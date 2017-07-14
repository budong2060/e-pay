package com.pay.client;

import java.util.Map;

/**
 * Created by admin on 2017/7/14.
 * 微信相关接口
 *
 */
public interface WxPayClient {

    /**
     * 调用微信统一下单接口
     * @param requestXml
     * @return
     */
    Map<String, String> doPrepare(String requestXml);

}
