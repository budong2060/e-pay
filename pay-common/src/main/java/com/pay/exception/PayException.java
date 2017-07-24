package com.pay.exception;

import com.pay.enums.PayResultEnum;

/**
 * Created by admin on 2017/6/28.
 */
public class PayException extends RuntimeException {

    private PayResultEnum resultEnum = PayResultEnum.UN_KNOWN_EXCEPTION;

    public PayException() {
        super();
    }

    public PayException(String message) {
        super(message);
    }

    public PayException(Throwable cause) {
        super(cause);
    }

    public PayException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayException(PayResultEnum resultEnum, Throwable cause) {
        super(cause);
        this.resultEnum = resultEnum;
    }

    public PayException(PayResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.resultEnum = resultEnum;
    }

    public PayException(PayResultEnum resultEnum, String message) {
        super(null == message ? resultEnum.getMessage() : message);
        this.resultEnum = resultEnum;
    }

    public PayResultEnum getResultEnum() {
        return resultEnum;
    }

    public void setResultEnum(PayResultEnum resultEnum) {
        this.resultEnum = resultEnum;
    }
}
