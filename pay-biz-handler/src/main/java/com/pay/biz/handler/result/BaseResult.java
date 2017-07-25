package com.pay.biz.handler.result;

import com.framework.process.result.Result;

/**
 * Created by admin on 2017/7/5.
 */
public abstract class BaseResult<T> implements Result {

    /**
     * 结果状态
     */
    protected Status status;

    /**
     * 信息码
     */
    protected String code;

    /**
     * 描述
     */
    protected String message;

    public BaseResult() {
    }

    public BaseResult(Status status) {
        this(status, status.getCode(), status.getMessage());
    }

    public BaseResult(Status status, String code) {
        this(status, code, status.getMessage());
    }

    public BaseResult(Status status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
