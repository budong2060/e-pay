package com.pay.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by admin on 2017/8/23.
 * 账单列表
 */
public class PayBillItem extends BaseDomain {
    /**
     * 账单日
     */
    private Date billDate;
    /**
     * 平台商户id
     */
    private String mchId;
    /**
     * 第三方支付商户id
     */
    private String thirdMchId;
    /**
     * 第三方支付子商户id
     */
    private String thirdSubMchId;
    /**
     * 业务订单号
     */
    private String orderNo;
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 交易时间
     */
    private Date tradeTime;
    /**
     * 商户订单号
     */
    private String tradeNo;
    /**
     * 第三方支付订单号
     */
    private String thirdTradeNo;
    /**
     * 交易状态
     */
    private Integer tradeStatus;
    /**
     * 订单总金额
     */
    private BigDecimal tradeAmount;
    /**
     * 代金券或立减优惠金额
     */
    private BigDecimal couponAmount;
    /**
     * 交易描述
     */
    private String tradeDesc;
    /**
     * 商户退款单号
     */
    private String refundNo;
    /**
     * 第三方退款单号
     */
    private String thirdRefundNo;
    /**
     * 退款申请时间
     */
    private Date refundTime;
    /**
     * 退款完成时间
     */
    private Date refundFinishTime;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 代金券或立减优惠退款金额
     */
    private BigDecimal refundCouponAmount;
    /**
     * 退款状态
     */
    private Integer refundStatus;
    /**
     * 支付渠道
     */
    private Integer payWay;
    /**
     * 账单类型 1:支付，2：退款
     */
    private Integer billType;

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getThirdMchId() {
        return thirdMchId;
    }

    public void setThirdMchId(String thirdMchId) {
        this.thirdMchId = thirdMchId;
    }

    public String getThirdSubMchId() {
        return thirdSubMchId;
    }

    public void setThirdSubMchId(String thirdSubMchId) {
        this.thirdSubMchId = thirdSubMchId;
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

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
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

    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getTradeDesc() {
        return tradeDesc;
    }

    public void setTradeDesc(String tradeDesc) {
        this.tradeDesc = tradeDesc;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public String getThirdRefundNo() {
        return thirdRefundNo;
    }

    public void setThirdRefundNo(String thirdRefundNo) {
        this.thirdRefundNo = thirdRefundNo;
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

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getRefundCouponAmount() {
        return refundCouponAmount;
    }

    public void setRefundCouponAmount(BigDecimal refundCouponAmount) {
        this.refundCouponAmount = refundCouponAmount;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PayBillItem that = (PayBillItem) o;

        if (!mchId.equals(that.mchId)) return false;
        if (!tradeNo.equals(that.tradeNo)) return false;
        if (!thirdTradeNo.equals(that.thirdTradeNo)) return false;
        if (!tradeAmount.equals(that.tradeAmount)) return false;
        if (!refundNo.equals(that.refundNo)) return false;
        if (!thirdRefundNo.equals(that.thirdRefundNo)) return false;
        return refundAmount.equals(that.refundAmount);

    }

    @Override
    public int hashCode() {
        int result = mchId.hashCode();
        result = 31 * result + tradeNo.hashCode();
        result = 31 * result + thirdTradeNo.hashCode();
        result = 31 * result + tradeAmount.hashCode();
        result = 31 * result + refundNo.hashCode();
        result = 31 * result + thirdRefundNo.hashCode();
        result = 31 * result + refundAmount.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PayBillItem{" +
                "billDate=" + billDate +
                ", mchId='" + mchId + '\'' +
                ", payWay=" + payWay +
                '}';
    }
}
