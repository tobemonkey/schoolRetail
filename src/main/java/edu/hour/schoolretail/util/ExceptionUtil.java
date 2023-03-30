package edu.hour.schoolretail.util;

import edu.hour.schoolretail.common.constant.enums.exception.ExceptionEnum;
import edu.hour.schoolretail.dto.Result;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 201926002057 戴毅
 * @Description 用于通过传入参数进行异常信息拼接
 * @Date 2023/2/11
 **/
public class ExceptionUtil {

    public static String buildMsg(String content, Exception e) {
        return String.format("%s", content + "， 错误信息：" + e.getMessage());
    }

    public static void putException(Map map, ExceptionEnum exception) {
        map.put("status", exception.getStatus());
        map.put("msg", exception.getMsg());
        map.put("url", exception.getUrl());
    }

    public static Result getResultMap(Map map) {
        String status = (String) map.get("status");
        map.remove("status");
        if (status.equals(ExceptionEnum.COMMON_SUCCESS.getStatus())) {
            return Result.success(map);
        } else if (status.startsWith("5")) {
            return Result.failForSystem(map);
        }
        return Result.failForUser(map);
    }
}
