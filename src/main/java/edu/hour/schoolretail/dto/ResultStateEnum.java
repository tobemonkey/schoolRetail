package edu.hour.schoolretail.dto;

/**
 * @Author 201926002057 戴毅
 * @Description
 * @Date 2023/1/9
 **/
public enum ResultStateEnum {

    // 操作成功
    SUCCESS("0001", "操作成功"),

    ACOUNT_LOGIN_BY_OTHERS("0002", "账号异地登入，已挤下，如不是本人操作，请尽快修改密码"),

    // 系统错误：代码以-100 开头
    SYSTEM_ERROR("-1001", "系统出错，请联系管理员，联系电话：18046863263"),

    // 用户操作错误：代码以-200 开头
    USER_OPERATE_ERROR("-2001", "用户操作不合法"),

    DOUBLE_KEY_REGISTER("-2002", "用户已被注册"),

    VERIFY_CODE_ERROR("-2003", "验证码错误"),

    USERNAME_OR_PASSWORD_ERROR("-2004", "账号或密码错误");

    // 其他异常，代码以 -300开头

    // 提示信息状态码
    private String stateCode;
    // 提示信息描述
    private String msg;

    ResultStateEnum(String stateCode, String msg) {
        this.stateCode = stateCode;
        this.msg = msg;
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getMsg() {
        return msg;
    }
}
