package edu.hour.schoolretail.common.constant.enums.exception;

/**
 * @Author 201926002057 戴毅
 * @Description token 检测错误的状态枚举
 * @Date 2023/2/7
 **/
public enum TokenExceptionEnum {

    TOKEN_EXPIRE(-1, "token 超时"),

    SIGN_ERROR(1, "签名异常"),

    ALGORITHM_MISMATCH(2, "算法不匹配"),

    OTHER_EXCEPTION(4, "其他异常");

    // 状态描述
    private int status;

    // 状态码
    private String desc;

    TokenExceptionEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
