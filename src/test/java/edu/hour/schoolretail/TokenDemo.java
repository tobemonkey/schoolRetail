package edu.hour.schoolretail;

import edu.hour.schoolretail.util.JWTUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author demoy
 * @create 19:44
 */
public class TokenDemo {

	@Test
	public void tokenTest() throws Exception {
		String token = JWTUtil.createToken(21654462315412L, 1);
		Map<String, String> map = JWTUtil.getClaimsMap(token);
		map.forEach((k, v) -> {
			System.out.println("key: " + k + " value:" + v);
		});
	}
}
