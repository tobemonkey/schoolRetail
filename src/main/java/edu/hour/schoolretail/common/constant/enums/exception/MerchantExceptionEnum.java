package edu.hour.schoolretail.common.constant.enums.exception;

/**
 * @author demoy
 * @version 1.0.0
 * @Description 商家异常枚举类
 * @createTime 2023年03月23日 00:17
 */
public enum MerchantExceptionEnum {

	// 通常信息以 0 开头
	COMMON_SUCCESS("0000", "操作成功", null),

	// 用户信息异常以 1 开头
	USER_TOKEN_ERROR("1001", "token 解析异常", null),

	// 商品操作失败以 2 开头
	GOODS_INSERT_FAIL("2001", "商品插入失败", null),

	GOODS_IMAGE_OVER_LIMIT("2002", "商品图片超过限制", null),

	GOODS_NOT_EXISTS("2003", "商品不存在", null),

	GOODS_HAVEN_FREEZE("2004", "商品已冻结", null);


	private final String status;

	private final String msg;

	private final String url;

	MerchantExceptionEnum(String status, String msg, String url) {
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
