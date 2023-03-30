package edu.hour.schoolretail.interceptor.role;

import edu.hour.schoolretail.common.constant.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月23日 15:39
 */
@Slf4j
@Component
public class MerchantInterceptor implements HandlerInterceptor {

	/**
	 * 用户权限判断，如果用户权限为店家则可以访问
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Map<String, Object> data = (Map<String, Object>) request.getAttribute("data");
		Integer role = (Integer) data.get("role");
		if (role.equals(Role.ROLE_MERCHANT)) {
			return true;
		}
		return false;
	}
}
