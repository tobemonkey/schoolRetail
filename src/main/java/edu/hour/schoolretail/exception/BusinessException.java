package edu.hour.schoolretail.exception;

/**
 * @Author 201926002057 戴毅
 * @Description 业务处理异常
 * @Date 2023/2/11
 **/
public class BusinessException extends RuntimeException {

    private String message;

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }


    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }


    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = message;
    }
}
