package com.pay.biz.handler.node.wx;

import com.framework.process.AbstractNode;
import com.framework.process.DefaultJobContext;
import com.pay.Constants;
import com.pay.client.WxPayClient;
import com.pay.domain.BaseDomain;
import com.pay.domain.PayConfig;
import com.pay.domain.PayPayment;
import com.pay.enums.PayResultEnum;
import com.pay.enums.PayWay;
import com.pay.exception.PayException;
import com.pay.biz.handler.result.PayResult;
import com.pay.mybatis.PayConfigMapper;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import util.MapUtil;
import util.PropertiesUtil;
import util.RemoteAdsUtil;
import util.WXUtil;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by admin on 2017/7/12.
 * 微信支付统一下单Node
 */
public class WXPayNode extends AbstractNode<BaseDomain, PayResult> {

    private final Logger logger = LoggerFactory.getLogger(WXPayNode.class);

    @Autowired
    private PayConfigMapper payConfigMapper;

    @Autowired
    protected WxPayClient wxPayClient;

    @Override
    public void process(DefaultJobContext<BaseDomain, PayResult> context, BaseDomain baseDomain) {
        PayPayment domain = (PayPayment) baseDomain;
        PayConfig payConfig = payConfigMapper.findOnly(domain.getMchId(), domain.getPayWay(), 1, 1);
        if(null == payConfig) {
            throw new PayException(PayResultEnum.DATA_HAS_NOT_EXSIT, "第三方账户不存在");
        }
        Map<String, String> config = payConfig.getConfig();

        //1.构造预支付参数
        Map<String, String> preParams = buildParams(config, domain);
        //2.预支付签名
        String sign = WXUtil.getSign(preParams, config.get(Constants.WX_PARTNER_KEY));
        logger.info(">>商户订单号:{},微信统一下单签名:{}", domain.getTradeNo(), sign);
        preParams.put("sign", sign);
        //3.预支付下单
        String prepareXml = MapUtil.map2Xml(preParams);
        Map<String, String> prepayResult = wxPayClient.doPrepare(prepareXml);
        if(!"SUCCESS".equals(prepayResult.get("result_code"))) {
            throw new PayException(PayResultEnum.PAY_PREPAY_FAIL, prepayResult.get("err_code_des"));
        }
        //4.返回结果
//        String prepayId = prepayResult.get("prepay_id");
        Map<String, String> resultData = buildResult(config, prepayResult);
        PayResult<Object> result = new PayResult<>();
        result.setData(resultData);
        result.setResultEnum(PayResultEnum.EXECUTE_SUCCESS);
        context.setResult(result);
        return;
    }

    /**
     * 构建预支付参数
     * @param config
     * @param payPayment
     * @return
     */
    protected Map<String, String> buildParams(Map<String, String> config, PayPayment payPayment) {
        Map<String, String> sortParams = new TreeMap<>();
        sortParams.put("appid", config.get(Constants.WX_APPID));
        sortParams.put("mch_id", config.get(Constants.WX_MCH_ID));
        sortParams.put("body", payPayment.getTradeDesc());
        sortParams.put("nonce_str", WXUtil.getNonceStr());
        sortParams.put("out_trade_no", payPayment.getTradeNo());
        sortParams.put("spbill_create_ip", RemoteAdsUtil.getRemoteAddress());
        sortParams.put("product_id", payPayment.getOrderNo());
//        sortParams.put("spbill_create_ip", payPayment.getRequestId());

        PayWay payWay = PayWay.getByCode(payPayment.getPayWay());
        sortParams.put("trade_type", payWay.getType());

        //设置回调地址，若有配置则取配置文件，否则取默认地址
        String uri = "/wx/pay/notify/" + payPayment.getMchId() + "/" + payPayment.getPayWay();
        String host = config.get(Constants.PAY_HOST);
        if(StringUtils.isEmpty(host)) {
            host = PropertiesUtil.getProperty(Constants.PAY_HOST);
        }
        sortParams.put("notify_url", host + uri);

        //计算金额，元转分
        BigDecimal tradeAmount = payPayment.getTradeAmount();
        int totalFee = tradeAmount.multiply(new BigDecimal(100)).intValue();
        sortParams.put("total_fee", String.valueOf(totalFee));
        //设置过期时间
        Date timeExpire = payPayment.getTimeExpire();
        if(null != timeExpire) {
            sortParams.put("time_expire", DateFormatUtils.format(timeExpire, "yyyyMMddHHmmss"));
        }
        return sortParams;
    }

    /**
     * 扩展参数
     * @param config
     * @param params
     * @param payPayment
     * @return
     */
    protected Map<String, String> extParams(Map<String, String> config, Map<String, String> params, PayPayment payPayment) {
        return params;
    }

    /**
     * 构造返回客户端数据
     * @param config
     * @param prepay
     * @return
     */
    private Map<String, String> buildResult(Map<String, String> config, Map<String, String> prepay) {
        String prepayId = prepay.get("prepay_id");
        String nonceStr = WXUtil.getNonceStr();
        String timestamp = WXUtil.getTimeStamp();
        String sign = genAppSign(config, prepayId, nonceStr, timestamp);

        Map<String, String> data = new HashMap<>();
        data.put("partnerId", config.get(Constants.WX_MCH_ID));
        data.put("prepayId", prepayId);
        data.put("package", "Sign=WXPay");
        data.put("nonceStr", nonceStr);
        data.put("timeStamp", timestamp);
        data.put("sign", sign);
        data.put("codeUrl", prepay.get("code_url"));
        return data;
    }


    /**
     * 构造微信统一下单参数
     * @param config
     * @param payPayment
     * @return
     */
    private String genPrepareXml(Map<String, String> config, PayPayment payPayment) {
        Map<String, String> sortParams = new TreeMap<>();
        sortParams.put("appid", config.get(Constants.WX_APPID));
        sortParams.put("mch_id", config.get(Constants.WX_MCH_ID));
        sortParams.put("body", payPayment.getTradeDesc());
        sortParams.put("nonce_str", WXUtil.getNonceStr());
        sortParams.put("out_trade_no", payPayment.getTradeNo());
        sortParams.put("spbill_create_ip", RemoteAdsUtil.getRemoteAddress());
//        sortParams.put("spbill_create_ip", payPayment.getRequestId());


        PayWay payWay = PayWay.getByCode(payPayment.getPayWay());
        sortParams.put("trade_type", payWay.getType());

        //设置回调地址，若有配置则取配置文件，否则取默认地址
        String notifyUrl = config.get(Constants.NOTIFY_URL);
        if(StringUtils.hasLength(notifyUrl)) {
            sortParams.put("notify_url", notifyUrl);
        } else {
            //TODO
            String host = PropertiesUtil.getProperty(Constants.PAY_HOST);
            sortParams.put("notify_url", host);
        }
        //计算金额，元转分
        BigDecimal tradeAmount = payPayment.getTradeAmount();
        int totalFee = tradeAmount.multiply(new BigDecimal(100)).intValue();
        sortParams.put("total_fee", String.valueOf(totalFee));
        //设置过期时间
        Date timeExpire = payPayment.getTimeExpire();
        if(null != timeExpire) {
            sortParams.put("time_expire", DateFormatUtils.format(timeExpire, "yyyyMMddHHmmss"));
        }
        String sign = WXUtil.getSign(sortParams, config.get(Constants.WX_PARTNER_KEY));
        logger.info(">>商户号:{},微信统一下单签名:{}", payPayment.getTradeNo(), sign);
        sortParams.put("sign", sign);
        return MapUtil.map2Xml(sortParams);
    }

    /**
     * 对返回给客户端所有参数签名
     * @param config
     * @param prepayId
     * @param nonceStr
     * @param timestamp
     * @return
     */
    private String genAppSign(Map<String, String> config, String prepayId, String nonceStr, String timestamp) {
        Map<String, String> sortParams = new TreeMap<>();
        sortParams.put("noncestr", nonceStr);
        sortParams.put("partnerid", config.get(Constants.WX_MCH_ID));
        sortParams.put("package", "Sign=WXPay");
        sortParams.put("prepayid", prepayId);
        sortParams.put("timestamp", timestamp);
        String sign = WXUtil.getSign(sortParams, config.get(Constants.WX_PARTNER_KEY));
        logger.info(">>预支付ID:{},微信APP拉起支付签名Sign:{}", prepayId, sign);
        return sign;
    }

}
