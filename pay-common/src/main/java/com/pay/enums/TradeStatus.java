package com.pay.enums;

/**
 *  订单交易状态
 * @author heyinbo
 * @date 2017年7月13日
 * @version 1.0
 */
public enum TradeStatus {
	/** 支付申请 */
	TRADE_APPLY(1, "支付申请"),
	/** 支付中 */
	TRADE_PROCEEDING(2, "支付中"),
	/** 支付成功 */
	TRADE_SUCCESS(3, "支付成功"),
	/** 支付失败 */
	TRADE_FAIL(4, "支付失败"),
	/** 支付订单已关闭 */
	TRADE_CLOSED(5, "支付订单已关闭"),
	;

	/**
	 * 枚举值
	 */
	private final int code;

	/**
	 * 枚举描述
	 */
	private final String message;
	

	TradeStatus(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int code() {
		return code;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * 通过枚举<code>code</code>获得枚举
	 *
	 * @param code
	 * @return TradeStatus
	 */
	public static TradeStatus getByCode(int code) {
		for (TradeStatus _enum : values()) {
			if (_enum.getCode() == code) {
				return _enum;
			}
		}
		return null;
	}

}