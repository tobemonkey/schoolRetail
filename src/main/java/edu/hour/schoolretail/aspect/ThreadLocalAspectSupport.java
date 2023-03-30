package edu.hour.schoolretail.aspect;

import java.util.Map;

/**
 * @Author 201926002057 戴毅
 * @Description 用于存储所有请求的请求信息，当异常发生时方便获取
 * @Date 2023/2/11
 **/
public class ThreadLocalAspectSupport {

    private static ThreadLocal<Map<String, String>> threadLocal;

    public static ThreadLocal<Map<String, String>> getThreadLocal() {
       return threadLocal == null ? threadLocal = new ThreadLocal<>() : threadLocal;
    }
}
