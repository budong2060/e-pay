package com.pay;

/**
 * Created by admin on 2017/7/13.
 */
public abstract class Constants {
    //============================通用常量===================================
    public static final String PAY_HOST = "pay.host";

    public static final String NOTIFY_URL = "notifyUrl";

    //============================微信常量===================================
    public static final String WX_APPID = "appId";

    public static final String WX_MCH_ID = "mchId";

    public static final String WX_PARTNER_KEY = "partnerKey";

    public static final String CERT_PATH = "certPath";

    public static final String CERT_PWD = "certPwd";

    public static final String APPSECRET = "appSecret";
    /**
     * 微信统一下单
     */
    public static final String WX_PAY_UNIFIEDORDER = "wx.pay.unifiedorder";
    /**
     * 微信退款
     */
    public static final String WX_PAY_REFUND = "wx.pay.refund";
    /**
     * 微信授权
     */
    public static final String WX_PAY_AUTHORIZE = "wx.pay.authorize";

//    public static String WX_JS_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static final String WX_ACCESS_TOKEN = "wx.access.token";

}
