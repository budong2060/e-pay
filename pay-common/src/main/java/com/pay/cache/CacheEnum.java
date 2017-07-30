package com.pay.cache;

/**
 * Created by admin on 2017/7/5.
 */
public enum  CacheEnum {
    NATIVE("native", "hashmap cache"),
    GUAVA("guava", "google guava cache"),
    REDIS("redis", "redis cache")

    ;

    /** 枚举值 */
    private final String code;

    /** 枚举描述 */
    private final String message;

    /**
     *
     * @param code
     * @param message
     */
    CacheEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
