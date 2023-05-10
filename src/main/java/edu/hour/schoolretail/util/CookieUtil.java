package edu.hour.schoolretail.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.cookie.CookieRestrictionViolationException;

import javax.servlet.http.Cookie;
import java.util.Map;

/**
 * @author demoy
 * @version 1.0.0
 * @Description cookie 工具类
 * @createTime 2023年03月29日 10:00
 */
@Slf4j
public class CookieUtil {

//	// 加密密钥
//	private static final String KEY = "X5JYYQq3kyOWC02jI/X+xBwRzJswG/Y=";
//	// todo 尝试做一个初始化向量，加在用户表中，用户首次使用查询数据库，后续使用通过 redis
//
//	private static SecretKeySpec key;
//
//	static {
//		key = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");
//	}


	private CookieUtil() {
	}


	/**
	 * 通过 name 和 value 创建一个 value 加密的 cookie
	 * @param name
	 * @param value
	 * @return
	 */
	public static Cookie getCookie(String name, String value) {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("cookie 的 name 不能为空");
		}
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("cookie 的 value 不能为空");
		}

		Cookie cookie = new Cookie(name, value);
		cookie.setDomain("localhost");
		cookie.setPath("/");
		// 关闭，因为不允许 websocket 请求携带cookie
		// cookie.setHttpOnly(true);
		cookie.setMaxAge(60 * 60);
		return cookie;
	}


	/**
	 * 解密 cookie
	 * @param cookie
	 * @return
	 * @throws CookieRestrictionViolationException
	 */
	public static Map<String, String> getValue(Cookie cookie) {
		String value = cookie.getValue();
		Map<String, String> claimsMap = JWTUtil.getClaimsMap(value);
		return claimsMap;
	}

	/**
	 * 通过设置过期时间删除 cookie
	 * @param name
	 * @return
	 */
	public static Cookie deleteCookie(String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setDomain("39b456804w.oicp.vip");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		return cookie;
	}

/*	private static String encryptCookieValue(String content) throws CookieRestrictionViolationException {

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
		} catch (NoSuchAlgorithmException e) {
			log.error("没有相关算法，错误信息：{}", e.getMessage());
			throw new IllegalArgumentException("没有相关算法", e);
		} catch (NoSuchPaddingException | InvalidKeyException e) {
			log.error("初始化加密对象失败！非法 key 值：{}，错误原因为：{}", key, e.getMessage());
			throw new IllegalArgumentException("初始化加密对象失败！非法 key 值：" + key, e);
		}

		byte[] encrypted = new byte[0];
		try {
			encrypted = cipher.doFinal(content.getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			log.error("cookie 加密信息失败，错误信息为：{}", e.getMessage());
			throw new CookieRestrictionViolationException();
		}
		return Base64.getEncoder().encodeToString(encrypted);
	}

	private static String decryptCookieValue(String secret) throws CookieRestrictionViolationException {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
		} catch (NoSuchAlgorithmException e) {
			log.error("没有相关算法，错误信息：{}", e.getMessage());
			throw new IllegalArgumentException("没有相关算法", e);
		} catch (NoSuchPaddingException | InvalidKeyException e) {
			log.error("初始化加密对象失败！非法 key 值：{}，错误原因为：{}", key.getEncoded().toString(), e.getMessage());
			throw new IllegalArgumentException("初始化加密对象失败！非法 key 值：" + key, e);
		}

		byte[] original = null;
		try {
			original = cipher.doFinal(Base64.getDecoder().decode(secret));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			log.error("cookie 解密信息失败，错误信息为：{}", e.getMessage());
			throw new CookieRestrictionViolationException();
		}
		return new String(original);
	}*/

}
