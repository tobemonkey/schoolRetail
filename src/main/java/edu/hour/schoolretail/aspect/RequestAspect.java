package edu.hour.schoolretail.aspect;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 201926002057 戴毅
 * @Description 对于所有请求做切面处理，主要附加功能为：打印请求信息相关日志
 * @Date 2023/2/10
 **/
@Aspect
@Component
@Slf4j
public class RequestAspect {

    private static final String MULTIPART_FILE = "MultipartFile";

    private static final String REQUEST_FACADE = "RequestFacade";

    private static final String RESPONSE_FACADE = "ResponseFacade";

    @Pointcut("execution(* edu.hour.schoolretail.controller..*(..))")
    public void aspectRequestInfo(){}

    @Before("aspectRequestInfo()")
    public void beforeRequest(JoinPoint joinPoint) {
        Map<String, String> map = new HashMap<>();

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            // 获取方法的名字以及功能名
            ApiOperation annotation = method.getAnnotation(ApiOperation.class);
            String methodInfo = "方法名：" + method.getName();
            if (annotation != null) {
                methodInfo += "，功能名：" + annotation.value();
            }
            // 存储相关信息，当方法报错时进行方法定位
            map.put("methodInfo", methodInfo);
            map.put("interfaceName", method.getName());
            ThreadLocalAspectSupport.getThreadLocal().set(map);
            // 日志打印方法信息以及参数信息
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (arg != null) {
                    String argType = arg.getClass().getName();
                    // 参数中包括不可直接打印参数则不打印
                    if (argType.contains(MULTIPART_FILE) || argType.contains(REQUEST_FACADE)
                            || argType.contains(RESPONSE_FACADE) || arg.toString().contains("password")) {
                        log.info("{}，入参无法打印", methodInfo);
                        return;
                    }
                }
            }
            log.info("{}，入参信息为：{}", methodInfo, args);
        } catch (Exception e) {
            log.error("日志打印出错！方法信息为：{}，错误信息为：{}", JSON.toJSONString(map), e.getMessage());
        }

    }

}
