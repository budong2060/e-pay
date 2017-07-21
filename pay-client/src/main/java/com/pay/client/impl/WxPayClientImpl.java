package com.pay.client.impl;

import com.pay.Constants;
import com.pay.client.WxPayClient;
import com.pay.enums.PayResultEnum;
import com.pay.exception.PayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import util.JsonUtil;
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
            String url = PropertiesUtil.getProperty(Constants.WX_PAY_UNIFIEDORDER);
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

    @Override
    public Map<String, String> accessToken(String appid, String secret, String code) {
        try {
            String accessTokenUrl = PropertiesUtil.getProperty(Constants.WX_ACCESS_TOKEN);
            String url = accessTokenUrl + "?appid={appid}" + "&secret={secret}" + "&code={code}" + "&grant_type=authorization_code";
            logger.info(">>调用微信获取openid请求地址:{},参数appid:{},code:{}", url, appid, code);
            Map<String, String> result = restTemplate.getForObject(url, Map.class, appid, secret, code);
            logger.info(">>调用微信获取openid请求结果:{}", JsonUtil.toString(result));
            return result;
        } catch (Exception e) {
            logger.error(">>调用微信获取openid接口失败,原因:{}", e);
            throw new PayException(PayResultEnum.NETWORK_EXCEPTION, "获取微信openId失败");
        }
    }

    @Override
    public Map<String, String> doRefund(String requestXml) {
        try {
            logger.info(">>调用微信退款接口请求入参:{}", requestXml);
            String url = PropertiesUtil.getProperty(Constants.WX_PAY_REFUND);
            requestXml = new String(requestXml.toString().getBytes(), "ISO8859-1");
            String responseXml = restTemplate.postForObject(url, requestXml, String.class);
            responseXml = new String(responseXml.getBytes("ISO-8859-1"), "UTF-8");
            logger.info(">>调用微信退款接口请求结果:{}", responseXml);
            return XMLUtil.doXMLParse(responseXml);
        } catch (Exception e) {
            logger.error(">>调用微信退款接口失败,原因:{}", e);
            throw new PayException(PayResultEnum.REFUND_FAIL);
        }
    }

    @Override
    public void other() {
        restTemplate.getForObject("http://localhost:8081/spring/boot/test", Object.class);
        logger.info("================");
    }
}
