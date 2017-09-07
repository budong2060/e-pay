package com.pay.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by admin on 2017/9/5.
 * 交易记录
 */
public class PayRecord extends BaseDomain {
    /**
     * 商户号
     */
    private String mchId;
    /**
     * 支付用户ID
     */
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
    private Integer payWay;
    /**
     * 交易类型 0=支付 1=退款
     */
    private Integer tradeType;
    /**
     * 商户交易流水号（支付|退款）
     */
    private String tradeNo;
    /**
     * 第三方交易流水号（支付|退款）
     */
    private String thirdTradeNo;
    /**
     * 交易金额
     */
    private BigDecimal tradeAmount = new BigDecimal(0.00);
    /**
     * 交易下单时间
     */
    private Date tradeTime = new Date();
    /**
     * 交易完成时间
     */
    private Date tradeFinishTime;

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

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
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

    @Override
    public String toString() {
        return "PayRecord{" +
                "mchId='" + mchId + '\'' +
                ", userId='" + userId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderType=" + orderType +
                ", payWay=" + payWay +
                ", tradeType=" + tradeType +
                ", tradeNo='" + tradeNo + '\'' +
                ", thirdTradeNo='" + thirdTradeNo + '\'' +
                ", tradeAmount=" + tradeAmount +
                ", tradeTime=" + tradeTime +
                ", tradeFinishTime=" + tradeFinishTime +
                '}';
    }

}
