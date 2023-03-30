package edu.hour.schoolretail.aspect;

import edu.hour.schoolretail.dto.Result;
import edu.hour.schoolretail.exception.BusinessException;
import edu.hour.schoolretail.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**
 * @Author 201926002057 戴毅
 * @Description 对 controller 抛出的全局异常进行处理
 * @Date 2023/2/11
 **/
@ControllerAdvice
@Slf4j
public class ControllerExceptionAspect {

    @ExceptionHandler(value = BusinessException.class)
    public Result businessExceptionHandler(BusinessException be) {
        String errorMsg = null;

        try {
            // 获取当前连接的相关信息，并进行打印
            Map<String, String> map = ThreadLocalAspectSupport.getThreadLocal().get();
            String methodInfo = map.get("methodInfo");
            String content = "失败 " + methodInfo + " 执行";
            errorMsg = ExceptionUtil.buildMsg(content, be);
            log.info(errorMsg);
        } catch (Exception e) {
            String exceptionMsg = "正在处理的异常为：" + be.getMessage();
            String method = "导致异常的方法信息：" + ThreadLocalAspectSupport.getThreadLocal().get();
            String content = "Business 异常处理失败，涉及到的信息如下：{" + exceptionMsg + "  " + method + "}";
            errorMsg = ExceptionUtil.buildMsg(content, e);
            log.error(errorMsg);
        }
        return Result.failForSystem(errorMsg);
    }
}
