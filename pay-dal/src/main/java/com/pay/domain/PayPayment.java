package com.pay.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by admin on 2017/7/5.
 */
public class PayPayment extends BaseDomain {
    /**
     * 商户号
     */
    @NotEmpty(message = "商户ID不能为空")
    private String mchId;
    /**
     * 支付用户ID
     */
    @NotEmpty(message = "支付用户ID不能为空")
    private String userId;
    /**
     * 订单号
     */
    @NotEmpty(message = "支付订单号不能为空")
    private String orderNo;
    /**
     * 订单类型
     */
    @NotNull(message = "支付订单类型不能为空")
    private Integer orderType;
    /**
     * 支付渠道
     */
    @NotNull(message = "支付渠道不能为空")
    private Integer payWay;
    /**
     * 商户流水号
     */
    private String tradeNo;
    /**
     * 支付金额
     */
    @DecimalMin(value = "0", message = "支付金额必须大于0")
    private BigDecimal tradeAmount = new BigDecimal(0.00);
    /**
     * 支付下单时间
     */
    private Date tradeTime;
    /**
     * 支付完成时间
     */
    private Date tradeFinishTime;
    /**
     * 过期时间
     */
    private Date timeExpire;
    /**
     * 支付状态
     */
    private Integer tradeStatus;
    /**
     * 第三方实际支付金额
     */
    private BigDecimal thirdTradeAmount = new BigDecimal(0.00);
    /**
     * 第三方支付流水号
     */
    private String thirdTradeNo;
    /**
     * 支付订单描述
     */
    private String tradeDesc;
    /**
     * 回调业务系统地址
     */
    @NotEmpty(message = "支付回调地址不能为空")
    private String notifyUrl;
    /**
     * 0-未通知，1-已通知
     */
    private Integer notifyCode;
    /**
     * 微信授权码
     */
    private String code;

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public Date getTradeFinishTime() {
        return tradeFinishTime;
    }

    public void setTradeFinishTime(Date tradeFinishTime) {
        this.tradeFinishTime = tradeFinishTime;
    }

    public Date getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(Date timeExpire) {
        this.timeExpire = timeExpire;
    }

    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public BigDecimal getThirdTradeAmount() {
        return thirdTradeAmount;
    }

    public void setThirdTradeAmount(BigDecimal thirdTradeAmount) {
        this.thirdTradeAmount = thirdTradeAmount;
    }

    public String getThirdTradeNo() {
        return thirdTradeNo;
    }

    public void setThirdTradeNo(String thirdTradeNo) {
        this.thirdTradeNo = thirdTradeNo;
    }

    public String getTradeDesc() {
        return tradeDesc;
    }

    public void setTradeDesc(String tradeDesc) {
        this.tradeDesc = tradeDesc;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getNotifyCode() {
        return notifyCode;
    }

    public void setNotifyCode(Integer notifyCode) {
        this.notifyCode = notifyCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
