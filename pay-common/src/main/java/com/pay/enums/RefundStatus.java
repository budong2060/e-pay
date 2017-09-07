package com.pay.enums;

/**
 *  订单交易状态
 * @author heyinbo
 * @date 2017年7月13日
 * @version 1.0
 */
public enum RefundStatus {

	REFUND_APPLY(1, "退款申请"),

	REFUND_PROCEEDING(2, "退款中"),

	REFUND_SUCCESS(3, "退款成功"),

	REFUND_FAIL(4, "退款失败"),

	REFUND_CLOSED(5, "退款订单已关闭"),

	REFUND_EXCEPTION(6, "退款异常"),

	;

	/**
	 * 枚举值
	 */
	private final int code;

	/**
	 * 枚举描述
	 */
	private final String message;


	RefundStatus(int code, String message) {
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
	public static RefundStatus getByCode(int code) {
		for (RefundStatus _enum : values()) {
			if (_enum.getCode() == code) {
				return _enum;
			}
		}
		return null;
	}

}