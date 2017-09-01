package com.pay.biz.handler.node.ali;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.pay.Constants;
import com.pay.domain.PayPayment;
import com.pay.enums.PayResultEnum;
import com.pay.exception.PayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import util.DateUtils;
import util.PropertiesUtil;

import java.util.Date;
import java.util.Map;

/**
 * Created by admin on 2017/8/29.
 * 支付宝网页支付
 */
public class AliPagePayNode extends AliPayNode {

    private final Logger logger = LoggerFactory.getLogger(AliPagePayNode.class);

    @Override
    protected Object doProcess(Map<String, String> config, PayPayment domain) {
        AlipayClient alipayClient = new DefaultAlipayClient(PropertiesUtil.getProperty(Constants.ALI_GATEWAY), config.get(Constants.ALI_APP_ID), config.get(Constants.ALI_PRIVATEKEY),
                "json", Constants.CHARSET, config.get(Constants.ALI_PUBLICKEY), config.get(Constants.RSA) == null ? "RSA" : config.get(Constants.RSA));
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request

        //设置回调地址，若有配置则取配置文件，否则取默认地址
        String host = config.get(Constants.PAY_HOST);
        if(StringUtils.isEmpty(host)) {
            host = PropertiesUtil.getProperty(Constants.PAY_HOST);
        }
        String notifyUri = "/ali/pay/notify/" + domain.getMchId() + "/" + domain.getPayWay();
        String returnUri = "/ali/pay/return/" + domain.getMchId() + "/" + domain.getPayWay();
        alipayRequest.setReturnUrl(host + returnUri);
        alipayRequest.setNotifyUrl(host + notifyUri);

        //model
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setTotalAmount(domain.getTradeAmount().toString());
        model.setOutTradeNo(domain.getTradeNo());
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setSubject(domain.getTradeDesc());
        model.setBody(domain.getTradeDesc());
        if (null != domain.getTimeExpire()) {
            long timeExpire = DateUtils.timeDiff(new Date(), domain.getTimeExpire());
            model.setTimeExpire(String.valueOf(timeExpire));
        }
        alipayRequest.setBizModel(model);
//        //构造参数
//        StringBuffer sb = new StringBuffer();
//        sb.append("{")
//                .append("\"out_trade_no\":").append(domain.getTradeNo()).append(",")
//                .append("\"product_code\":").append("\"FAST_INSTANT_TRADE_PAY\"").append(",")
//                .append("\"total_amount\":").append(domain.getThirdTradeAmount()).append(",")
//                .append("\"subject\":").append(domain.getTradeDesc()).append(",")
//                .append("\"body\":").append(domain.getTradeDesc());
//        if (null != domain.getTimeExpire()) {
//            long timeExpire = DateUtils.timeDiff(new Date(), domain.getTimeExpire());
//            sb.append(",").append("\"timeout_express\":").append(timeExpire);
//        }
//        sb.append("}");
//        alipayRequest.setBizContent(sb.toString());//填充业务参数
        try {
            String form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            logger.info(">>支付宝PC网页构造支付参数结果:{}", form);
            return form;
        } catch (AlipayApiException e) {
            logger.error(">>发起支付宝网页交易请求服务端参数组装失败:", e);
            throw new PayException(PayResultEnum.PAY_PREPAY_FAIL, "构造支付宝参数失败");
        }
    }
}
