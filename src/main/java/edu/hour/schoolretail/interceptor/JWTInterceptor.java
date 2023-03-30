package edu.hour.schoolretail.interceptor;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hour.schoolretail.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 201926002057 戴毅
 * @Description jwt 拦截器，进行权限校验
 * @Date 2023/2/7
 **/
@Slf4j
@Component
public class JWTInterceptor implements HandlerInterceptor {

    /**
     * 进行权限校验，token 超时状态值为 -1，其他校验失败情况状态值为 1；成功则直接放行
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IllegalArgumentException, Exception {
        String token = request.getHeader("token");
        Map<String, Object> retMap = new HashMap<>();
        // 状态值：token 非法异常则为 -1；否则为0，成功则放行
        retMap.put("status", 1);
        try {
            Map<String, String> decodeToken = JWTUtil.getClaimsMap(token);
            request.getSession().setAttribute("data", JSONUtil.toJsonStr(decodeToken));
            log.info("token 声明信息为：{}", decodeToken);
            return true;
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
            retMap.put("status", 0);
        } catch (Exception e) {
            log.error(e.getMessage());
            retMap.put("status", -1);
        }

        String json = new ObjectMapper().writeValueAsString(retMap);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }

    /**
     * 进行 token更新
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String token = request.getHeader("token");
        // 因为 prehandler 中进行了 token 验证，所以不需要在进行 token验证，直接获取对应的 id 并更新 token 即可
        Map<String, String> claimsMap = JWTUtil.getClaimsMap(token);
        Long id = Long.valueOf(claimsMap.get("id"));
        Integer role = Integer.valueOf(claimsMap.get("role"));
        String newToken = JWTUtil.createToken(id, role);
        request.getSession().setAttribute("token", newToken);
    }
}
