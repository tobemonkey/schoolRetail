package edu.hour.schoolretail.exception;

/**
 * @Author 201926002057 戴毅
 * @Description
 * @Date 2023/1/9
 **/
public class LoginAndRegisterException extends BusinessException{

    private String message;

    public LoginAndRegisterException(String message) {
        super(message);
        this.message = message;
    }


    public LoginAndRegisterException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }


    public LoginAndRegisterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = message;
    }
}
