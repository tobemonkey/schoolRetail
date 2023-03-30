package edu.hour.schoolretail.common.constant.enums.exception;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月14日 10:06
 */
public enum ExceptionEnum {

	COMMON_SUCCESS("0000", "操作成功", null),

	COMMON_VERIFY_ERROR("0001", "验证码超时或不匹配", null),

	USER_NOT_EXISTS("1001", "用户不存在", null),

	REGISTER_HAD_USER("2001", "该用户已存在！", "https://39b456804w.oicp.vip/loginAndRegister"),
	// 用户信息过期
	AUTH_USER_INFO_EXPIRE("3001", "验证时用户信息过期", "https://39b456804w.oicp.vip/loginAndRegister"),

	AUTH_TOKEN_EXPIRE("3002", "验证时token过期","https://39b456804w.oicp.vip/loginAndRegister"),

	OTHERS_EXCEPTION("4000", "其他异常", null),

	SYSTEM_EXCEPTION("5000", "系统异常", null);


	private final String status;

	private final String msg;

	private final String url;

	ExceptionEnum(String status, String msg, String url) {
		this.status = status;
		this.msg = msg;
		this.url = url;
	}

	public String getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}

	public String getUrl() {
		return url;
	}
}
