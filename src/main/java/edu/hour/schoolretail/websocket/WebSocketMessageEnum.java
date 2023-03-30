package edu.hour.schoolretail.websocket;

/**
 * @author demoy
 * @create 11:10
 */
public enum WebSocketMessageEnum {

	LOGIN("0001", "登入"),

	REPEAT_LOGIN("-0001", "您的账号异地登入，请注意账号安全");



	private String status;

	private String msg;

	WebSocketMessageEnum (String status, String msg) {
		this.msg = msg;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}
}
