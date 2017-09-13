package com.pay.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付结果状态枚举
 */
public enum PayResultEnum {
	
	//====================通用start==========================
	/** 未知异常 */
	UN_KNOWN_EXCEPTION("UN_KNOWN_EXCEPTION", "未知异常", "000"),
	/** 数据库错误 **/
	DB_EXCEPTION("DB_EXCEPTION", "数据库错误", "001"),
	/** IO异常 **/
	IO_EXCEPTION("IO_EXCEPTION", "IO异常", "100"),
	/** 网络异常 **/
	NETWORK_EXCEPTION("NETWORK_EXCEPTION", "网络异常", "101"),
	/** 程序错误 */
	PROGRAM_ERROR("PROGRAM_ERROR", "程序错误", "500"),
	/** 执行成功 */
	EXECUTE_SUCCESS("EXECUTE_SUCCESS", "执行成功", "200"),
	/** 请求参数非法 */
	ILLEGAL_ARGUMENTS("ILLEGAL_ARGUMENTS", "请求参数非法", "403"),
	/** 流水已经存在 */
	ORDER_NO_IS_EXIST("ORDER_NO_IS_EXIST", "流水已经存在", "405"),
	/** 请求参数为空 */
	REQ_PARAM_IS_NULL("REQ_PARAM_IS_NULL", "请求参数为空", "401"),
	/** 系统异常 */
	SYSTEM_EXCEPTION("SYSTEM_EXCEPTION", "系统异常", "501"),
	/** 数据异常 */
	DATA_EXCEPTION("DATA_EXCEPTION", "数据异常", "002"),
	/** 无此数据 */
	DATA_HAS_NOT_EXSIT("DATA_HAS_NOT_EXSIT", "数据不存在", "003"),
	/** 该数据已经存在 */
	DATA_HAS_EXSIT("DATA_HAS_EXSIT", "该数据已经存在", "004"),
	/** 签名验证失败 */
	SIGN_VERIFY_FIAL("SIGN_VERIFY_FIAL", "签名验证失败", "405"),
	/** 处理中 */
	PROCESSING("PROCESSING", "处理中", "100"),
	//====================共同end==========================

	//====================支付start 2开头==========================

	PAY_HAS_FINISH("PAY_HAS_FINISH", "该支付已经完成", "201"),

	PAY_RESULT_PARSE_FAIL("PAY_RESULT_PARSE_FAIL", "支付结果解析失败", "204"),

	PAY_PREPAY_FAIL("PAY_PREPAY_FAIL", "预支付失败", "205"),

	PAY_FAIL("PAY_FAIL", "支付失败", "206"),

	PAY_AUTHORIZE_FAIL("PAY_AUTHORIZE_FAIL", "支付授权失败", "207"),

	INVALID_TRADE_NO("INVALID_TRADE_NO", "无效商户订单号", "208"),

	//====================退款 start 3开头==========================
	REFUND_AMOUNT_OVERDUE("REFUND_AMOUNT_OVERDUE", "退款金额超限", "301"),

	REFUND_FAIL("REFUND_FAIL", "退款失败", "303"),

	//====================对账 start 4开头==========================
	FAIL_DOWNLOAD_BILL("FAIL_DOWNLOAD_BILL", "下载对账单失败", "501"),


	;

	/**
	 * 枚举值
	 */
	private final String code;

	/**
	 * 枚举描述
	 */
	private final String message;

	/**
	 * 信息码
	 */
	private final String status;
	
	/**
	 *
	 * @param code
	 * @param message
	 * @param status
	 */
	PayResultEnum(String code, String message, String status) {
		this.code = code;
		this.message = message;
		this.status = status;
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @return Returns the code.
	 */
	public String code() {
		return code;
	}
	
	/**
	 * @return Returns the message.
	 */
	public String message() {
		return message;
	}

	public String getStatus() {
		return status;
	}

	/**
	 * 通过枚举<code>code</code>获得枚举
	 * 
	 * @param code
	 * @return PayResultEnum
	 */
	public static PayResultEnum getByCode(String code) {
		for (PayResultEnum _enum : values()) {
			if (_enum.getCode().equals(code)) {
				return _enum;
			}
		}
		return null;
	}
	
	/**
	 * 通过枚举<code>status</code>获得枚举
	 * 
	 * @param status
	 * @return PayResultEnum
	 */
	public static PayResultEnum getByErrorCode(String status) {
		for (PayResultEnum _enum : values()) {
			if (_enum.getStatus().equals(status)) {
				return _enum;
			}
		}
		return null;
	}
	
	/**
	 * 获取全部枚举
	 * 
	 * @return List<PayResultEnum>
	 */
	public List<PayResultEnum> getAllEnum() {
		List<PayResultEnum> list = new ArrayList<PayResultEnum>();
		for (PayResultEnum _enum : values()) {
			list.add(_enum);
		}
		return list;
	}
	
	/**
	 * 获取全部枚举值
	 * 
	 * @return List<String>
	 */
	public List<String> getAllEnumCode() {
		List<String> list = new ArrayList<String>();
		for (PayResultEnum _enum : values()) {
			list.add(_enum.code());
		}
		return list;
	}
}
