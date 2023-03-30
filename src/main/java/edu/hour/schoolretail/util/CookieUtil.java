package edu.hour.schoolretail.util;

import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author demoy
 * @version 1.0.0
 * @Description cookie 工具类
 * @createTime 2023年03月29日 10:00
 */
public class CookieUtil {

	// 盐
	private static final String SALT = "*TEUw%JIew893UIUert";

	private CookieUtil() {
	}


	/**
	 * 获取加密的 cookie
	 * @param name
	 * @param value
	 * @return
	 */
	public static Cookie generateEncryptCookie(String name, String value) {
		return null;
	}

	/**
	 * 把 Cookie 添加到 response 中
	 *
	 * @param cookie   Cookie to add
	 * @param response Response to add Cookie to
	 *
	 * @return true unless cookie or response is null
	 */
	public static boolean addCookie(final Cookie cookie, final HttpServletResponse response) {
		if (cookie == null || response == null) {
			return false;
		}

		response.addCookie(cookie);
		return true;
	}

	/**
	 * 通过 cookieName 获取对应的 cookie
	 *
	 * @param request    Request to get the Cookie from
	 * @param cookieName name of Cookie to get
	 *
	 * @return the named Cookie, null if the named Cookie cannot be found
	 */
	public static Cookie getCookie(final HttpServletRequest request, final String cookieName) {
		if (StringUtils.isBlank(cookieName)) {
			return null;
		}

		final Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}

		if (cookies.length > 0) {
			for (final Cookie cookie : cookies) {
				if (StringUtils.equals(cookieName, cookie.getName())) {
					return cookie;
				}
			}
		}

		return null;
	}

}
