package edu.hour.schoolretail.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author demoy
 * @version 1.0.0
 * @Description 轻度token验证，针对既可以做 token，又可以不做token的请求
 * @createTime 2023年03月28日 15:05
 */
@Component
@Slf4j
public class LightJWTInterceptor implements HandlerInterceptor {

	@Resource
	private JWTInterceptor jwtInterceptor;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = request.getHeader("token");
		if (token != null) {
			return jwtInterceptor.preHandle(request, response, handler);
		}
		return true;
	}

}
