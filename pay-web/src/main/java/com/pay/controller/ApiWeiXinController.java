package com.pay.controller;

import com.pay.Constants;
import com.pay.domain.PayConfig;
import com.pay.domain.PayPayment;
import com.pay.enums.PayResultEnum;
import com.pay.enums.PayWay;
import com.pay.enums.TradeStatus;
import com.pay.exception.PayException;
import com.pay.biz.handler.result.PayResult;
import com.pay.service.PayConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import util.MD5Util;
import util.MapUtil;
import util.PropertiesUtil;
import util.XMLUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by admin on 2017/7/17.
 * 微信支付相关
 */
@RestController
public class ApiWeiXinController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ApiWeiXinController.class);

    @Autowired
    private PayConfigService payConfigService;

    /**
     * 微信授权入口
     * @param response
     * @param mchId
     * @param payWay
     */
    @RequestMapping(value = "/wx/pay/authorize/{mchId}/{payWay}", method = RequestMethod.GET)
    public void authorize(HttpServletResponse response,
                            @PathVariable String mchId, @PathVariable Integer payWay) {
        PayConfig payConfig = payConfigService.getOnly(mchId, payWay, 1, 1);
        if (null == payConfig || null == payConfig.getConfig()) {
            throw new PayException(PayResultEnum.DATA_HAS_NOT_EXSIT, "第三方支付账户配置不存在");
        }
        Map<String, String> config = payConfig.getConfig();
        try {
            String host = PropertiesUtil.getProperty(Constants.PAY_HOST);
            String redirectUri = host + "/pay/wx/authorize/callback";
            String authorizeUrl = PropertiesUtil.getProperty(Constants.WX_PAY_AUTHORIZE);
            String url = authorizeUrl
                    + "?"
                    + "appid="
                    + config.get(Constants.WX_APPID)
                    + "&redirect_uri="
                    + redirectUri
                    + "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
            logger.info(">>微信授权申请:{}", url);
            response.sendRedirect(url);
        } catch (Exception e) {
            logger.info(">>微信授权申请异常:{}", e);
            throw new PayException(PayResultEnum.PAY_AUTHORIZE_FAIL);
        }
    }

    /**
     * 授权回调接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/wx/pay/authorize/callback", method = RequestMethod.GET)
    public PayResult redirectUri(HttpServletRequest request) {
        PayResult result = new PayResult();
        String code = request.getParameter("code");
        if(StringUtils.isEmpty(code)) {
            result.setResultEnum(PayResultEnum.PAY_AUTHORIZE_FAIL);
            return result;
        }
        result.setData(code);
        result.setResultEnum(PayResultEnum.EXECUTE_SUCCESS);
        return result;
    }

    /**
     * 微信支付通知接口
     * @param mchId
     * @param payWay
     * @return
     */
    @RequestMapping(value = "/wx/pay/notify/{mchId}/{payWay}")
    public Object payNotify(HttpServletRequest request, @PathVariable String mchId, @PathVariable Integer payWay) {
        logger.info(">>收到微信支付异步通知请求,商户号:{}, 渠道:{}", mchId, payWay);
        PayConfig payConfig = payConfigService.getOnly(mchId, payWay, 1, 1);
        if (null == payConfig || null == payConfig.getConfig()) {
            throw new PayException(PayResultEnum.DATA_HAS_NOT_EXSIT, "第三方支付账户配置不存在");
        }
        Map<String, String> result= new HashMap<>();
        Map<String, String> config = payConfig.getConfig();
        TreeMap<String, String> requestData = null;
        try {
            StringBuffer sb = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            logger.info(">>收到微信支付异步通知参数:{}", sb);
            requestData = XMLUtil.doXMLParseForTreeMap(sb.toString());
        } catch (Exception e) {
            logger.error(">>解析微信支付异步通知参数异常:{}", e);
            result.put("return_code", "FAIL");
            result.put("return_msg", "parse request data fail");
            return MapUtil.map2Xml(result);
        }
        //验签
        if (!"SUCCESS".equals(requestData.get("return_code")) ||
                !verifySign(requestData, config.get(Constants.WX_PARTNER_KEY))) {
            result.put("return_code", "FAIL");
            result.put("return_msg", "验证签名失败");
            return MapUtil.map2Xml(result);
        }
        PayPayment payment = new PayPayment();
        payment.setTradeNo(requestData.get("out_trade_no"));
        if(StringUtils.hasLength(requestData.get("trade_type"))) {
            PayWay realPayWay = PayWay.getByType(requestData.get("trade_type"));
            payment.setPayWay(realPayWay.getCode());
        }

        BigDecimal totalFee = new BigDecimal(requestData.get("total_fee"));
        payment.setThirdTradeAmount(totalFee.divide(new BigDecimal(100)));
        payment.setTradeFinishTime(new Date());
        payment.setThirdTradeNo(requestData.get("transaction_id"));
        //初始化回调参数
        if("SUCCESS".equals(requestData.get("result_code"))) {
            payment.setTradeStatus(TradeStatus.TRADE_SUCCESS.code());
        } else {
            payment.setTradeStatus(TradeStatus.TRADE_FAIL.code());
            payment.setTradeDesc(requestData.get("err_code_des"));
        }


        result.put("return_code", "SUCCESS");
        return MapUtil.map2Xml(result);
    }

    /**
     * 微信退款通知接口
     * @param request
     * @param mchId
     * @param payWay
     * @return
     */
    @RequestMapping(value = "/wx/refund/notify/{mchId}/{payWay}")
    public Object refundNotify(HttpServletRequest request, @PathVariable String mchId, @PathVariable Integer payWay) {

        return null;
    }

    /**
     * 微信回调签名验证
     * @param params
     * @param key
     * @return
     */
    private boolean verifySign(Map<String, String> params, String key) {
        String wxSign = params.get("sign").toString().toLowerCase();
        params.remove("sign");
        String urlParams = MapUtil.map2UrlParams(params);
        urlParams = urlParams + "&key=" + key;
        // 算出摘要
        String sign = MD5Util.MD5Encode(urlParams, "UTF-8").toLowerCase();
        return wxSign.equals(sign);
    }

}
