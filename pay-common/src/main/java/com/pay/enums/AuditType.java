package com.pay.enums;

/**
 * 审核类型
 */
public enum AuditType {
	/**
	 * 人工审核
	 */
	CUSTOMER(1, "人工审核"),
	/**
	 * 自动审核
	 */
	AUTO(0, "自动审核通过");

	/**
	 * 枚举值
	 */
	private final int code;

	/**
	 * 枚举描述
	 */
	private final String message;

	AuditType(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public static AuditType getByCode(int code) {
		for (AuditType _enum : values()) {
			if (_enum.getCode() == code) {
				return _enum;
			}
		}
		return null;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
