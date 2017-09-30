package com.pay.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/18.
 * 支付渠道
 */
public enum PayWay {

    WX_PAY(14, "JSAPI", "WX", "微信公众号"),

    WX_APP_PAY(16, "APP", "WX", "微信APP"),

    WX_NATIVE_PAY(18, "NATIVE", "WX", "微信扫码"),

    ALI_PAY(10, "ALI_PAY", "ALI", "支付宝"),

    ALI_APP_PAY(11, "ALI_APP_PAY", "ALI", "支付宝APP"),

    ALI_WAP_PAY(12, "ALI_WAP_PAY", "ALI", "支付宝WAP"),

    CMBC_PAY(16, "CMBC_PAY", "CMBC", "招行支付"),
    ;

    /**
     * 枚举值
     */
    private final int code;

    /**
     * 支付类型
     */
    private final String type;
    /**
     * 渠道
     */
    private final String channel;

    /**
     * 枚举描述
     */
    private final String message;

    PayWay(int code, String type, String channel, String message) {
        this.code = code;
        this.type = type;
        this.channel = channel;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code
     * @return PayWay
     */
    public static PayWay getByCode(int code) {
        for (PayWay _enum : values()) {
            if (_enum.getCode() == code) {
                return _enum;
            }
        }
        return null;
    }

    public static PayWay getByType(String type) {
        for (PayWay _enum : values()) {
            if (type.equals(_enum.getType())) {
                return _enum;
            }
        }
        return null;
    }

    public String getChannel() {
        return channel;
    }

    /**
     * 获取全部枚举值
     *
     * @return List<String>
     */
    public static List<String> getAllChannel() {
        List<String> list = new ArrayList<String>();
        for (PayWay _enum : values()) {
            list.add(_enum.getChannel());
        }
        return list;
    }

}
