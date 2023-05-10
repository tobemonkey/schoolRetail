package edu.hour.schoolretail.controller.common;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.hour.schoolretail.dto.Result;
import edu.hour.schoolretail.dto.UserAccessToken;
import edu.hour.schoolretail.entity.WechatUser;
import edu.hour.schoolretail.util.wechat.SignUtils;
import edu.hour.schoolretail.util.wechat.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

/**
 * @author demoy
 * @create 10:23
 */
@Controller
@RequestMapping("/connect/wechat")
@Slf4j
public class WechatController {

	@Value("${spring.wechat.appid}")
	private String appId;

	@Value("${spring.wechat.appsecret}")
	private String appSecret;

	/**
	 * 初次连接使用，正常使用情况下不会被使用到
	 * @param request
	 * @param response
	 */
	public void connect(HttpServletRequest request, HttpServletResponse response) {
		log.info("尝试进行微信连接");
		// 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		try (PrintWriter out = response.getWriter()) {
			if (SignUtils.checkSignature(signature, timestamp, nonce)) {
				log.info("微信连接成功....");
				out.print(echostr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 对 code 链接进行拼接，并通过重定向的方式返回给用户进行访问，该页面就是微信扫码页面
	 * @return
	 */
	@GetMapping("/getQrCode")
	public String getQrCode() {
		//微信开放平台授权baseUrl %s相当于?代表占位符
		String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
				"?appid=%s" +
				"&redirect_uri=%s" +
				"&response_type=code" +
				"&scope=snsapi_login,snsapi_userinfo" +
				"&state=%s" +
				"#wechat_redirect";

		//对redirect_rul进行URLEncode编码
		String redirectUrl = "http://localhost:8080/loginAndRegister";
		try {
			redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		String url = String.format(
				baseUrl,
				appId,
				redirectUrl,
				"1654641s5df45s4546"
		);

		// 二维码所在的重定向地址
		return "redirect:" + url;
	}

/*
	@GetMapping("/callback")
	public String callback(String code, String state) {
		String accessTokenUrlFormat =
				"https://api.weixin.qq.com/sns/oauth2/access_token" +
						"?appid=%s" +
						"&secret=%s" +
						"&code=%s" +
						"&grant_type=authorization_code";

		String accessTokenUrl = String.format(accessTokenUrlFormat, appId, appSecret, code);
		try {
			String args = HttpUtil.get(accessTokenUrl);
			JSONObject jsonObject = JSON.parseObject(args);
			String accessToken = jsonObject.getString("access_token");
			String openid = jsonObject.getString("openid");
			String unionid = jsonObject.getString("unionid");
		} catch (Exception e) {
			log.error("获取用户为新数据失败，错误信息：{}", e);
		}

	}*/



	/**
	 *
	 * @param request
	 */
	@GetMapping("/getWechatInfo")
	public void getWechatInfo(HttpServletRequest request) {
		// 获取微信公众号传输过来的code,通过code可获取access_token,进而获取用户信息
		String code = request.getParameter("code");
		// 这个state可以用来传我们自定义的信息，方便程序调用，这里也可以不用
		// String roleType = request.getParameter("state");
		log.info("微信登入 code: {}", code);

		UserAccessToken accessToken;
		String openId;
		WechatUser user;
		if (code != null) {
			try {
				accessToken = WechatUtils.getUserAccessToken(code);
				log.info("微信 AccessToken:" + accessToken.toString());
				// 通过token获取accessToken
				String token = accessToken.getAccessToken();
				// 通过token获取openId
				openId = accessToken.getOpenId();
				// 通过access_token和openId获取用户昵称等信息
				user = WechatUtils.getUserInfo(token, openId);
				log.info("微信登入用户信息:" + user.toString());
				request.getSession().setAttribute("openId", openId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}
