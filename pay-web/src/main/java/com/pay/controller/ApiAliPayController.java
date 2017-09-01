package com.pay.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.pay.Constants;
import com.pay.biz.handler.result.PayResult;
import com.pay.domain.PayConfig;
import com.pay.domain.PayPayment;
import com.pay.enums.AliTradingStatus;
import com.pay.enums.TradeStatus;
import com.pay.service.PayConfigService;
import com.pay.service.PayPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import util.DateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by admin on 2017/8/30.
 * 支付宝支付相关操作
 */
@Controller
public class ApiAliPayController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ApiAliPayController.class);

    @Autowired
    private PayConfigService payConfigService;

    @Autowired
    private PayPaymentService payPaymentService;

    /**
     * 支付宝同步返回
     * @param request
     * @param response
     * @param mchId
     * @param payWay
     */
    @RequestMapping(value = "/ali/pay/return/{mchId}/{payWay}")
    public void payReturn(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable String mchId, @PathVariable Integer payWay) {
        if (logger.isInfoEnabled()) {
            logger.info(">>收到支付宝支付同步返回请求,商户号:{} , 渠道:{}", mchId, payWay);
        }
        boolean verifyResult = verifySign(mchId, payWay, request);
        if (verifyResult) {
            // 商户订单号
            String tradeNo = request.getParameter("out_trade_no");
            // 支付宝交易号
            String thirdTradeNo = request.getParameter("trade_no");
            //交易金额
            String tradeAmount = request.getParameter("total_amount");
            // 买家支付宝账号 buyer_email
//            String thirdMchId = request.getParameter("buyer_logon_id");
            PayPayment payPayment = new PayPayment();
            payPayment.setMchId(mchId);
            payPayment.setPayWay(payWay);
            payPayment.setTradeNo(tradeNo);
            payPayment.setThirdTradeNo(thirdTradeNo);
            payPayment.setThirdTradeAmount(new BigDecimal(tradeAmount));
            payPayment.setTradeStatus(TradeStatus.TRADE_PROCEEDING.code());
            PayResult<PayPayment> result = payPaymentService.update(payPayment);
            payPayment = result.getData();
            if (null != payPayment && StringUtils.hasLength(payPayment.getReturnUrl())) {
                try {
                    response.sendRedirect(payPayment.getReturnUrl());
                } catch (IOException e) {
                    logger.error(">>支付宝-支付同步重定向业务系统异常", e);
                }
            }
        }
    }

    /**
     * 支付宝异步通知
     * @param request
     * @param mchId
     * @param payWay
     * @return
     */
    @RequestMapping(value = "/ali/pay/notify/{mchId}/{payWay}")
    public void payNotify(HttpServletRequest request, HttpServletResponse response,
                            @PathVariable String mchId, @PathVariable Integer payWay) {
        if (logger.isInfoEnabled()) {
            logger.info(">>收到支付宝支付异步通知请求,商户号:{}, 渠道:{}", mchId, payWay);
        }

        boolean verifyResult = verifySign(mchId, payWay, request);
        if (verifyResult) {
            // 商户订单号
            String tradeNo = request.getParameter("out_trade_no");
            // 支付宝交易号
            String thirdTradeNo = request.getParameter("trade_no");
            // 交易状态
            String tradeStatus = request.getParameter("trade_status");
            // 交易付款时间
            String tradeTime = request.getParameter("gmt_payment");
            // 商品描述body
//            String tradeDesc = request.getParameter("body");
            // 交易金额
            String tradeAmount = request.getParameter("total_amount");
            // 买家支付宝账号 buyer_email
//            String thirdMchId = request.getParameter("buyer_logon_id");
            PayPayment payPayment = new PayPayment();
            if (tradeStatus.equals(AliTradingStatus.TRADE_FINISHED.getCode()) ||
                    tradeStatus.equals(AliTradingStatus.TRADE_SUCCESS.getCode())) {
                payPayment.setTradeStatus(TradeStatus.TRADE_SUCCESS.code());
            } else if (tradeStatus.equals(AliTradingStatus.TRADE_CLOSED.getCode())) {
                payPayment.setTradeStatus(TradeStatus.TRADE_FAIL.code());
            } else {
                writer(response, "fail");
                return;
            }
            payPayment.setMchId(mchId);
            payPayment.setPayWay(payWay);
            payPayment.setTradeNo(tradeNo);
            payPayment.setThirdTradeNo(thirdTradeNo);
            payPayment.setTradeFinishTime(DateUtils.parse(tradeTime));
            payPayment.setThirdTradeAmount(new BigDecimal(tradeAmount));
            payPaymentService.finishPay(payPayment);
            writer(response, "success");
            return;
        }
        writer(response, "fail");
        return;
    }

    /**
     * 验证支付宝回掉签名
     * @param mchId
     * @param payWay
     * @param request
     * @return
     */
    private boolean verifySign(String mchId, int payWay, HttpServletRequest request) {
        Map<String, String[]> notifyData = request.getParameterMap();
        Map<String, String> params = createSignParams(notifyData);

        PayConfig payConfig = payConfigService.getOnly(mchId, payWay, 1, 1);
        if (null == payConfig) {
            logger.info(">>未找到对应的支付渠道,商户号:{}, 渠道:{}", mchId, payWay);
            return false;
        }

        Map<String, String> config = payConfig.getConfig();
        if (null == config) {
            logger.info(">>对应的支付渠道配置错误,商户号:{}, 渠道:{}", mchId, payWay);
            return false;
        }

        boolean verifyResult = false;
        try {
            verifyResult = AlipaySignature.rsaCheckV1(params, config.get(Constants.ALI_PUBLICKEY), Constants.CHARSET,
                    config.get(Constants.RSA) == null ? "RSA" : config.get(Constants.RSA));
        } catch (Exception e) {
            logger.error(">>支付宝回调通知签名验证失败，mchId:{}", mchId);
        }
        return verifyResult;
    }


    /**
     * 构建支付宝回掉参数
     * @param notifyData
     * @return
     */
    private Map<String, String> createSignParams(Map<String, String[]> notifyData) {
        Map<String, String> params = new TreeMap<>();
        for (Iterator iter = notifyData.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) notifyData.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            params.put(name, valueStr);
        }
        return params;
    }

}
