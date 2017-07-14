package com.pay.client.impl;

import com.pay.Constants;
import com.pay.client.WxPayClient;
import com.pay.enums.PayResultEnum;
import com.pay.exception.PayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import util.PropertiesUtil;
import util.XMLUtil;

import java.util.Map;

/**
 * Created by admin on 2017/7/14.
 */
@Component
public class WxPayClientImpl implements WxPayClient {

    private final Logger logger = LoggerFactory.getLogger(WxPayClientImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Map<String, String> doPrepare(String requestXml) {
        try {
            logger.info(">>调用微信统一下单接口请求入参:{}", requestXml);
            String url = PropertiesUtil.getProperty(Constants.WX_PAY_UNIFIEDORDER_URL);
            requestXml = new String(requestXml.toString().getBytes(), "ISO8859-1");
            String responseXml = restTemplate.postForObject(url, requestXml, String.class);
            responseXml = new String(responseXml.getBytes("ISO-8859-1"), "UTF-8");
            logger.info(">>调用微信统一下单接口请求结果:{}", responseXml);
            return XMLUtil.doXMLParse(responseXml);
        } catch (Exception e) {
            logger.error(">>调用微信统一下单接口失败,原因:{}", e);
            throw new PayException(PayResultEnum.NETWORK_EXCEPTION, "获取微信预支付ID失败");
        }
    }
}
