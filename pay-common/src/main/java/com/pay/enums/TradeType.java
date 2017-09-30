package com.pay.enums;

/**
 *  交易类型
 * @author heyinbo
 * @date 2017年7月13日
 * @version 1.0
 */
public enum TradeType {

	PAY(1),      //支付

	REFUND(2),   //退款

	RECHARGE(3), //充值

	CASH(4),     //提现

	;

	/**
	 * 枚举值
	 */
	private final int code;

	TradeType(int code) {
		this.code = code;
	}

	public int code() {
		return code;
	}

	public int getCode() {
		return code;
	}

	/**
	 * 通过枚举<code>code</code>获得枚举
	 *
	 * @param code
	 * @return TradeType
	 */
	public static TradeType getByCode(int code) {
		for (TradeType _enum : values()) {
			if (_enum.getCode() == code) {
				return _enum;
			}
		}
		return null;
	}

}