package edu.hour.schoolretail.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @Author 201926002057 戴毅
 * @Description
 * @Date 2023/1/9
 **/
@Data
@Slf4j
public class Result {

    // 传输的数据，也可以是状态描述
    private Object data;

    // 操作是否成功
    private String status;

    private String msg;

    // 无需进行响应
    public Result(){}

    // 需要进行响应
    public Result(Object data, ResultStateEnum stateEnum){
        this.data = data;
        this.status = stateEnum.getStateCode();
        this.msg = stateEnum.getMsg();
    }


    /**
     * 操作执行成功并需要进行数据响应
     * @param data 响应数据
     */
    public static Result success(Object data) {
        return new Result(data, ResultStateEnum.SUCCESS);
    }

    /**
     * 用户操作执行失败并需要进行数据响应
     * @param data 响应数据
     */
    public static Result failForUser(Object data) {
        return new Result(data, ResultStateEnum.USER_OPERATE_ERROR);
    }

    /**
     * 系统操作执行失败并需要进行数据响应
     * @param data 响应数据
     */
    public static Result failForSystem(Object data) {
        return new Result(data, ResultStateEnum.SYSTEM_ERROR);
    }



}
