package edu.hour.schoolretail.util.wechat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 微信请求校验工具类
 */
public class SignUtils {
	// 与接口配置信息中的Token要一致
	private final static String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjpbeyJ0b29sdHQiOiJodHRwczovL3Rvb2x0dC5jb20ifV0sImlhdCI6MTY3ODE1NTY2MSwiZXhwIjoxNzA5NzQwNzk5LCJhdWQiOiIiLCJpc3MiOiJkZW1veSIsInN1YiI6IiJ9.Dah1spc8QxQqnhO0iwD5FNLP2yvLQmE1KSYuRubGeWI";

	/**
	 * 验证签名
	 *
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		String[] arr = new String[] { TOKEN, timestamp, nonce };
		// 将token、timestamp、nonce三个参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (String s : arr) {
			content.append(s);
		}
		MessageDigest md = null;
		String tmpStr = null;

		try {
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return tmpStr != null && tmpStr.equals(signature.toUpperCase());
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 *
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		StringBuilder strDigest = new StringBuilder();
		for (byte b : byteArray) {
			strDigest.append(byteToHexStr(b));
		}
		return strDigest.toString();
	}

	/**
	 * 将字节转换为十六进制字符串
	 *
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];

		return new String(tempArr);
	}
}