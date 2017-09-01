package com.pay.client;

import java.util.Map;

/**
 * Created by admin on 2017/7/14.
 * 微信相关接口
 *
 */
public interface WxPayClient {
    /**
     * 获取微信openId
     * @param appid
     * @param secret
     * @param code
     * @return
     */
    Map<String, String> accessToken(String appid, String secret, String code);

    /**
     * 调用微信统一下单接口
     * @param requestXml
     * @return
     */
    Map<String, String> doPrepare(String requestXml);

    /**
     * 微信退款
     * @param requestXml
     * @return
     */
    Map<String, String> doRefund(String certPath, String password, String requestXml);

    /**
     * 下载账单
     * @return
     */
    String downloadBill(String requestXml);
}
