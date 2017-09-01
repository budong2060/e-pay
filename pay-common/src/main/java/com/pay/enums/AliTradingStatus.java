package com.pay.enums;

/**
 *  订单交易状态
 * @author ChenPeng
 * @date 2015年3月16日
 * @version 1.0
 */
public enum AliTradingStatus {
	//--------支付宝退款状态start-----------//
	WAIT_SELLER_AGREE("WAIT_SELLER_AGREE", "退款协议等待卖家确认中"),
	SELLER_REFUSE_BUYER("SELLER_REFUSE_BUYER", "卖家不同意协议，等待买家修改"),
	WAIT_BUYER_RETURN_GOODS("WAIT_BUYER_RETURN_GOODS", "退款协议达成，等待买家退货"),
	WAIT_SELLER_CONFIRM_GOODS("WAIT_SELLER_CONFIRM_GOODS", "等待卖家收货"),
	REFUND_SUCCESS("REFUND_SUCCESS", "退款成功"),
	REFUND_CLOSED("REFUND_CLOSED", "退款关闭"),
	WAIT_ALIPAY_REFUND("WAIT_ALIPAY_REFUND", "等待支付宝退款"),
	ACTIVE_REFUND("ACTIVE_REFUND", "进行中的退款，供查询"),
	OVERED_REFUND("OVERED_REFUND", "结束的退款"),
	ALL_REFUND_STATUS("ALL_REFUND_STATUS", "所有退款，供查询"),
	//--------支付宝退款状态end-----------//
	
	//-----交易状态start----------//
	WAIT_BUYER_PAY("WAIT_BUYER_PAY","等待买家付款"),
	WAIT_SELLER_SEND_GOODS("WAIT_SELLER_SEND_GOODS","买家已付款，等待卖家发货"),
	WAIT_BUYER_CONFIRM_GOODS("WAIT_BUYER_CONFIRM_GOODS","卖家已发货，等待买家确认"),
	TRADE_FINISHED("TRADE_FINISHED","交易成功结束"),
	TRADE_CLOSED("TRADE_CLOSED","交易中途关闭（已结束，未成功完成）"),
	WAIT_SYS_CONFIRM_PAY("WAIT_SYS_CONFIRM_PAY","支付宝确认买家银行汇款中，暂勿发货"),
	WAIT_SYS_PAY_SELLER("WAIT_SYS_PAY_SELLER","买家确认收货，等待支付宝打款给卖家"),
	TRADE_REFUSE("TRADE_REFUSE","立即支付交易拒绝"),
	TRADE_REFUSE_DEALING("TRADE_REFUSE_DEALING","立即支付交易拒绝中"),
	TRADE_CANCEL("TRADE_CANCEL","立即支付交易取消"),
	TRADE_PENDING("TRADE_PENDING","等待卖家收款"),
	TRADE_SUCCESS("TRADE_SUCCESS","支付成功"),
	BUYER_PRE_AUTH("BUYER_PRE_AUTH","买家已付款（语音支付）"),
	COD_WAIT_SELLER_SEND_GOODS("COD_WAIT_SELLER_SEND_GOODS","等待卖家发货（货到付款）"),
	COD_WAIT_BUYER_PAY("COD_WAIT_BUYER_PAY","等待买家签收付款（货到付款）"),
	COD_WAIT_SYS_PAY_SELLER("COD_WAIT_SYS_PAY_SELLER","签收成功等待系统打款给卖家（货到付款）"),
	//-----交易状态end----------//
	;
	
	
	private String code;
	private String name;
	

	AliTradingStatus(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}