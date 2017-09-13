package com.pay.vo;

import com.pay.domain.PayPayment;
import org.hibernate.validator.constraints.NotEmpty;
import util.BeanUtil;
import util.MD5Util;
import util.MapUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 前端接收参数VO
 * Created by admin on 2017/9/13.
 */
public class PaymentVo implements Serializable {

    @NotNull(message = "时间戳不能为空")
    private Long time;

    @NotEmpty(message = "签名不能为空")
    private String sign;

    @NotNull(message = "支付数据不能为空")
    @Valid
    private PayPayment payPayment;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public PayPayment getPayPayment() {
        return payPayment;
    }

    public void setPayPayment(PayPayment payPayment) {
        this.payPayment = payPayment;
    }

    /**
     * 验证签名
     * @param key
     * @return
     */
    public boolean verify(String key) {
        if (payPayment != null) {
            String uri = MapUtil.map2UrlParams(BeanUtil.bean2Map(payPayment));
            String verifySign = MD5Util.MD5Encode(uri + "&key=" + key, "UTF-8").toUpperCase();
            return verifySign.equals(sign);
        }
        return false;
    }

    @Override
    public String toString() {
        return "PaymentVo{" +
                "time=" + time +
                ", sign='" + sign + '\'' +
                ", payPayment=" + payPayment +
                '}';
    }
}
