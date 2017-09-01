package com.pay.biz.handler.node.ali;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
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
 * 支付宝APP支付
 */
public class AliAppPayNode extends AliPayNode {

    private final Logger logger = LoggerFactory.getLogger(AliAppPayNode.class);

    @Override
    protected Object doProcess(Map<String, String> config, PayPayment domain) {
        AlipayClient alipayClient = new DefaultAlipayClient(PropertiesUtil.getProperty(Constants.ALI_GATEWAY), config.get(Constants.ALI_APP_ID), config.get(Constants.ALI_PRIVATEKEY),
                "json", Constants.CHARSET, config.get(Constants.ALI_PUBLICKEY), config.get(Constants.RSA) == null ? "RSA" : config.get(Constants.RSA));
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(domain.getTradeDesc());
        model.setSubject("ali app pay");
        model.setOutTradeNo(domain.getTradeNo());
        //设置过期时间
        if (null != domain.getTimeExpire()) {
            long timeExpire = DateUtils.timeDiff(new Date(), domain.getTimeExpire());
            model.setTimeoutExpress(timeExpire + "m");
        }
        model.setTotalAmount(String.valueOf(domain.getTradeAmount()));
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);

        //设置回调地址，若有配置则取配置文件，否则取默认地址
        String uri = "/ali/pay/notify/" + domain.getMchId() + "/" + domain.getPayWay();
        String host = config.get(Constants.PAY_HOST);
        if(StringUtils.isEmpty(host)) {
            host = PropertiesUtil.getProperty(Constants.PAY_HOST);
        }
        request.setNotifyUrl(host + uri);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            String data = alipayClient.sdkExecute(request).getBody();
            logger.info(">>发起支付宝app交易请求服务端参数组装结果:{}", data);
            return data;
        } catch (AlipayApiException e) {
            logger.error(">>发起支付宝app交易请求服务端参数组装失败:", e);
            throw new PayException(PayResultEnum.PAY_PREPAY_FAIL, "构造支付宝参数失败");
        }
    }
}
