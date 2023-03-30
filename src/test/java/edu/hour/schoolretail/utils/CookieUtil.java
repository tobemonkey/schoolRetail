package edu.hour.schoolretail.utils;

import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月29日 10:44
 */
public class CookieUtil {

	public static Cookie generateCookie(String value, String name) {
		return new Cookie(name, value);
	}

	@Test
	public void test() {
		Cookie cookie = generateCookie("123", "name");
		System.out.println(cookie);
	}
}
