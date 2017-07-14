package com.pay.logic.result;

/**
 * 返回结果状态。
 * 
 */
public enum Status {
	
	SUCCESS("success", "成功"),

	FAIL("fail", "失败"),
	
	PROCESSING("processing", "处理中");

	/**
	 * 状态码
	 */
	private final String code;

	/**
	 * 描述
	 */
	private final String message;
	
	/**
	 * @param code 枚举值码。
	 * @param message 枚举描述。
	 */
	Status(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	/**
	 * 得到枚举值码。
	 * @return 枚举值码。
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * 得到枚举描述。
	 * @return 枚举描述。
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * 得到枚举值码。
	 * @return 枚举值码。
	 */
	public String code() {
		return code;
	}
	
	/**
	 * 得到枚举描述。
	 * @return 枚举描述。
	 */
	public String message() {
		return message;
	}
	
}
