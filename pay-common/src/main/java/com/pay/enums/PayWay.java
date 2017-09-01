package com.pay.enums;

/**
 * Created by admin on 2017/7/18.
 * 支付渠道
 */
public enum PayWay {

    WX_PAY(14, "JSAPI", "微信公众号"),

    WX_APP_PAY(16, "APP", "微信APP"),

    WX_NATIVE_PAY(18, "NATIVE", "微信扫码"),

    ALI_PAY(10, "ALI_PAY", "支付宝"),

    ALI_APP_PAY(11, "ALI_APP_PAY", "支付宝APP"),

    ALI_WAP_PAY(12, "ALI_WAP_PAY", "支付宝WAP"),

    CMBC_PAY(16, "CMBC_PAY", "招行支付"),
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
     * 枚举描述
     */
    private final String message;


    PayWay(int code, String type, String message) {
        this.code = code;
        this.type = type;
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

}
