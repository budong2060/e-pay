package com.pay.client.impl;

import com.pay.Constants;
import com.pay.client.WxPayClient;
import com.pay.enums.PayResultEnum;
import com.pay.exception.PayException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import util.JsonUtil;
import util.PropertiesUtil;
import util.XMLUtil;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
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
    public Map<String, String> doRefund(String certPath, String password, String requestXml) {
        try {
            RestTemplate sslRestTemplate = sslRestTemplate(certPath, password);
            logger.info(">>调用微信退款接口请求入参:{}", requestXml);
            String url = PropertiesUtil.getProperty(Constants.WX_PAY_REFUND);
            requestXml = new String(requestXml.toString().getBytes(), "ISO8859-1");
            String responseXml = sslRestTemplate.postForObject(url, requestXml, String.class);
            responseXml = new String(responseXml.getBytes("ISO-8859-1"), "UTF-8");
            logger.info(">>调用微信退款接口请求结果:{}", responseXml);
            return XMLUtil.doXMLParse(responseXml);
        } catch (Exception e) {
            logger.error(">>调用微信退款接口失败,原因:{}", e);
            throw new PayException(PayResultEnum.REFUND_FAIL);
        }
    }

    @Override
    public String downloadBill(String requestXml) {
        //微信账单：失败时返回xml格式，成功时返回文本格式
        String responseXml = null;
        try {
            logger.info(">>调用微信下载对账单接口请求入参:{}", requestXml);
            String url = PropertiesUtil.getProperty(Constants.WX_DOWNLOAD_BILL);
            requestXml = new String(requestXml.toString().getBytes(), "ISO8859-1");
            responseXml = restTemplate.postForObject(url, requestXml, String.class);
            responseXml = new String(responseXml.getBytes("ISO-8859-1"), "UTF-8");
            logger.info(">>调用微信下载对账单接口请求结果:{}", responseXml);
            Map<String, String> result = XMLUtil.doXMLParse(responseXml);
            if("FAIL".equals(result.get("return_code"))){
                throw new PayException(PayResultEnum.FAIL_DOWNLOAD_BILL, result.get("return_msg"));
            }
        } catch (JDOMException | IOException e) {
            if (null == responseXml) {
                throw new PayException(PayResultEnum.FAIL_DOWNLOAD_BILL);
            }
        }
        return responseXml;
    }

    /**
     * 构造SSL RestTemplate
     * @param certPath 证书地址
     * @param password 证书密钥
     * @return
     */
    private RestTemplate sslRestTemplate(String certPath, String password) {
        logger.info(">>构造SSLRestTemplate证书:{}", certPath);
        FileInputStream instream = null;
        try {
            KeyStore keyStore  = KeyStore.getInstance("PKCS12");
            instream = new FileInputStream(new File(certPath));//P12文件目录
            keyStore.load(instream, password.toCharArray());
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, password.toCharArray())
                    .build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[] { "TLSv1" },
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            HttpComponentsClientHttpRequestFactory factory = (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
            factory.setHttpClient(httpclient);
            RestTemplate sslRestTemplate = new RestTemplate(factory);
            return sslRestTemplate;
        } catch (Exception e) {
            logger.error(">>获取证书失败,原因:", e);
        } finally {
            if (null != instream) {
                try {
                    instream.close();
                    } catch (Exception e) {
                        logger.error(">>关闭流失败,原因:", e);
                    }
            }
        }
        throw new PayException(PayResultEnum.SYSTEM_EXCEPTION);
    }
}
