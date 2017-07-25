package com.pay.biz.handler.result;

import com.pay.enums.PayResultEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by admin on 2017/7/5.
 */
public class PayResult<T> extends BaseResult {
    /**
     * 默认返回状态
     */
    private PayResultEnum resultEnum = PayResultEnum.EXECUTE_SUCCESS;

    private T data;

    public PayResult() {
        super();
    }

    public PayResult(T t) {
        this(PayResultEnum.EXECUTE_SUCCESS, t);
    }

    public PayResult(PayResultEnum resultEnum, T data) {
        setResultEnum(resultEnum);
        this.data = data;
    }

    public PayResultEnum getResultEnum() {
        return resultEnum;
    }

    public void setResultEnum(PayResultEnum resultEnum) {
        this.resultEnum = resultEnum;
        if (resultEnum == PayResultEnum.EXECUTE_SUCCESS) {
            super.status = Status.SUCCESS;
        } else if (resultEnum == PayResultEnum.PROCESSING) {
            super.status = Status.PROCESSING;
        } else {
            super.status = Status.FAIL;
        }
        super.code = resultEnum.getStatus();
        super.message = resultEnum.message();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
