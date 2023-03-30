//package edu.hour.schoolretail.interceptor;
//
//import com.auth0.jwt.exceptions.AlgorithmMismatchException;
//import com.auth0.jwt.exceptions.SignatureVerificationException;
//import com.auth0.jwt.exceptions.TokenExpiredException;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import edu.hour.schoolretail.common.constant.enums.exception.TokenExceptionEnum;
//import edu.hour.schoolretail.util.JWTUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @Author 201926002057 戴毅
// * @Description 用户登入拦截器，包括身份校验
// * @Date 2023/2/1
// **/
//@Component
//@Slf4j
//public class LoginInterceptor implements HandlerInterceptor {
//
//    /**
//     * 检测是否使用 token 进行登入
//     * @param request
//     * @param response
//     * @param handler
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String token = request.getHeader("token");
//        Map<String, Object> retMap = new HashMap<>();
//        // 状态值：超时状态值为 -1，todo 记住提示登入身份已过期，其他校验失败情况状态值为 1；成功则直接放行
//        retMap.put("status", 1);
//        // hasToken 属性作用：为 null 说明不是 login 请求，为 true 则表示携带 token
//        if (!StringUtils.isBlank(token)) {
//            Map<String, Object> decodeToken = null;
//            try {
//                decodeToken = JWTUtil.getClaimsMap(token);
//                // 将 token 解析数据传递给 controller
//                request.getSession().setAttribute("data", decodeToken);
//                log.info("token 声明信息为：{}", decodeToken);
//                return true;
//            } catch (TokenExpiredException e) {
//                log.error("token 超时！token 为：{}，错误信息为：{}", token, e.getMessage());
//                retMap.put("desc", TokenExceptionEnum.TOKEN_EXPIRE.getDesc());
//                retMap.put("status", TokenExceptionEnum.TOKEN_EXPIRE.getStatus());
//            } catch (SignatureVerificationException e) {
//                log.error("签名异常！token 为：{}，错误信息为：{}", token, e.getMessage());
//            } catch (AlgorithmMismatchException e) {
//                log.error("算法不匹配！token 为：{}，错误信息为：{}", token, e.getMessage());
//            } catch (Exception e) {
//                log.error("其他异常！token 为：{}，请求信息为：{}，错误信息为：{}", token, request.getRequestURL(), e);
//            }
//            String json = new ObjectMapper().writeValueAsString(retMap);
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().println(json);
//            return false;
//        }
//
//        return true;
//    }
//
//}
