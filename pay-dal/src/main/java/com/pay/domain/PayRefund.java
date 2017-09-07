package com.pay.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by admin on 2017/7/20.
 * 退款
 */
public class PayRefund extends BaseDomain {
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
    private String orderNo;
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 支付渠道
     */
    @NotNull(message = "商户流水号不能为空")
    private Integer payWay;
    /**
     * 商户支付流水号
     */
    @NotEmpty(message = "商户流水号不能为空")
    private String tradeNo;
    /**
     * 第三方支付流水号
     */
    private String thirdTradeNo;
    /**
     * 商户支付流水号
     */
    private String refundNo;
    /**
     * 总金额金额
     */
    private BigDecimal totalAmount = new BigDecimal(0.00);
    /**
     * 退款金额
     */
    private BigDecimal refundAmount = new BigDecimal(0.00);
    /**
     * 支付下单时间
     */
    private Date refundTime;
    /**
     * 支付完成时间
     */
    private Date refundFinishTime;
    /**
     * 支付状态
     */
    private Integer refundStatus;
    /**
     * 第三方支付流水号
     */
    private String thirdRefundNo;
    /**
     * 支付订单描述
     */
    private String refundDesc;
    /**
     * 回调业务系统地址
     */
    private String notifyUrl;
    /**
     * 0-未通知，1-已通知
     */
    private Integer notifyCode;

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

    public String getThirdTradeNo() {
        return thirdTradeNo;
    }

    public void setThirdTradeNo(String thirdTradeNo) {
        this.thirdTradeNo = thirdTradeNo;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Date getRefundFinishTime() {
        return refundFinishTime;
    }

    public void setRefundFinishTime(Date refundFinishTime) {
        this.refundFinishTime = refundFinishTime;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getThirdRefundNo() {
        return thirdRefundNo;
    }

    public void setThirdRefundNo(String thirdRefundNo) {
        this.thirdRefundNo = thirdRefundNo;
    }

    public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
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
