package edu.hour.schoolretail.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hour.schoolretail.common.constant.Role;
import edu.hour.schoolretail.common.constant.enums.exception.ExceptionEnum;
import edu.hour.schoolretail.common.constant.RedisKeys;
import edu.hour.schoolretail.dto.LoginAndRegisterDto;
import edu.hour.schoolretail.entity.GithubUser;
import edu.hour.schoolretail.entity.User;
import edu.hour.schoolretail.exception.BusinessException;
import edu.hour.schoolretail.exception.LoginAndRegisterException;
import edu.hour.schoolretail.mapper.UserMapper;
import edu.hour.schoolretail.service.GithubUserService;
import edu.hour.schoolretail.mapper.GithubUserMapper;
import edu.hour.schoolretail.util.EmailUtil;
import edu.hour.schoolretail.util.ExceptionUtil;
import edu.hour.schoolretail.util.JWTUtil;
import edu.hour.schoolretail.util.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author demoy
 * @description 针对表【t_github_user(github 认证用户的信息)】的数据库操作Service实现
 * @createDate 2023-03-09 15:16:21
 */
@Service
@Slf4j
public class GithubUserServiceImpl extends ServiceImpl<GithubUserMapper, GithubUser>
		implements GithubUserService{

	@Value("${spring.github.clientid}")
	private String clientId;

	@Value("${spring.redirectUrl}")
	private String redirectUrl;

	@Value("${spring.github.clientsecret}")
	private String clientSecret;

	@Value("${mail.verify.expire}")
	private Long expire;

	@Resource
	private UserMapper userMapper;

	@Resource
	private GithubUserMapper githubUserMapper;


	private static final String INDEX = "https://39b456804w.oicp.vip";

	@Resource
	private EmailUtil emailUtil;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;


	/**
	 * 回调函数，完成以下任务：
	 *      1. 获取用户信息并保存数据库，
	 *      2. 通过用户信息返回到主页
	 * @param code
	 * @return
	 */
	@Override
	public Map<String, String> callback(String code) {

		Map<String, String> map = new HashMap<>();

		String accessToken = getAccessTokenByCode(code);

		// 获取用户信息
		String body = HttpUtil.createGet("https://api.github.com/user")
				.bearerAuth(accessToken)
				.execute()
				.body();
		// 将 github 用户信息封装成对象
		GithubUser githubUser = JSON.parseObject(body, GithubUser.class);

		// 查看该用户是否已经注册
		User user = userMapper.selectByGithubId(githubUser.getId());
		// 如果没有登入过，就先进行注册，并将用户信息存储在 redis
		if (user == null) {
			long id = SnowFlakeUtil.nextId();
			String token = JWTUtil.createToken(id, Role.ROLE_USER, 10, Calendar.MINUTE);
			map.put("url", INDEX + "/login/oauth2/github/bind/" + token);
			// 存储用户信息
			redisTemplate.opsForValue().set(String.valueOf(id), githubUser, Duration.ofMinutes(10));
			return map;
		}
		// 如果有对应的用户，就取出用户信息并返回主页面
		map.put("url", INDEX);
		map.put("data", JSON.toJSONString(user));
		return map;
/*
		try {
			githubUserMapper.insert(githubUser);
		} catch (Exception e) {
			log.error("GithubUserServiceImpl：callback 用户信息插入失败，失败原因：{}", e.getMessage());
			return false;
		}

		return
				;*/
	}

	@Override
	public void sendVerifyCode(String target) {
		String verifyCode = null;
		try {
			verifyCode = emailUtil.sendVerifyCode(target);
			if (verifyCode == null) {
				log.error("验证码生成异常！");
				throw new LoginAndRegisterException("验证码生成异常！");
			}
			// 将验证码保存到 redis
			redisTemplate.opsForValue().set(RedisKeys.KEY_GITHUB_AUTH + target, verifyCode, expire, TimeUnit.MINUTES);
		} catch (MessagingException e) {
			log.error("邮件发送时出错！");
		}

		log.info("{} 的验证码为：{}", target, verifyCode);
	}

	@Override
	@Transactional
	public Map<String, Object> commit(String token, LoginAndRegisterDto loginAndRegisterDto) {
		HashMap<String, Object> map = new HashMap<>();
		Long githubInfoId = null;
		try {
			githubInfoId = JWTUtil.getUserIdByToken(token);
		} catch (TokenExpiredException e) {
			// token 超时，重新登入
			ExceptionUtil.putException(map, ExceptionEnum.AUTH_TOKEN_EXPIRE);
			return map;
		} catch (Exception e) {
			ExceptionUtil.putException(map, ExceptionEnum.OTHERS_EXCEPTION);
			return map;
		}

		// 验证码比对
		String verify = (String) redisTemplate.opsForValue().get(RedisKeys.KEY_GITHUB_AUTH + loginAndRegisterDto.getEmail());
		if (!loginAndRegisterDto.getVerifyCode().equals(verify)) {
			ExceptionUtil.putException(map, ExceptionEnum.COMMON_VERIFY_ERROR);
			return map;
		}

		// 查看邮箱已经被绑定，
		Long userId = userMapper.getUserIdByEmail(loginAndRegisterDto.getEmail());
		if (userId != null) {
			// 如果存在就将该账号与github账号绑定
			bindGithub(githubInfoId, userId, loginAndRegisterDto.getPassword(), map);
		} else {
			// 记录用户信息以及对应的github信息
			registerUserInfo(githubInfoId, loginAndRegisterDto, map);
		}

		return map;
	}

	/**
	 * 绑定用户与 github
	 * @param githubInfoId
	 * @param userId
	 */
	private void bindGithub(Long githubInfoId, Long userId, String password, HashMap<String, Object> map) {
		GithubUser githubUser = (GithubUser) redisTemplate.opsForValue().get(String.valueOf(githubInfoId));
		if (githubUser == null) {
			ExceptionUtil.putException(map, ExceptionEnum.AUTH_USER_INFO_EXPIRE);
			return;
		}
		githubUser.setUserId(userId);
		githubUserMapper.insert(githubUser);
		ExceptionUtil.putException(map, ExceptionEnum.COMMON_SUCCESS);
		User temp = userMapper.selectByGithubId(githubUser.getId());
		userMapper.updatePassword(userId, password);
		map.put("url", INDEX);
		map.put("data", temp);
	}

	/**
	 * 通过提交的数据从redis中取出杜英用户的 github 信息，并进行对应的 User 类的补充
	 * @param id
	 * @param loginAndRegisterDto
	 * @param map
	 */
	private void registerUserInfo(Long id, LoginAndRegisterDto loginAndRegisterDto, HashMap<String, Object> map) {
		User user = loginAndRegisterDto.toUser();
		GithubUser githubUser = (GithubUser) redisTemplate.opsForValue().get(String.valueOf(id));
		if (githubUser == null) {
			ExceptionUtil.putException(map, ExceptionEnum.AUTH_USER_INFO_EXPIRE);
			return;
		}

		// 设置用户信息
		user.setEmail(loginAndRegisterDto.getEmail());
		user.setPassword(loginAndRegisterDto.getPassword());
		user.setId(id);
		user.setImg(githubUser.getAvatarUrl());
		user.setSign(githubUser.getBio());
		user.setNickname(githubUser.getLogin());
		user.setCreateTime(LocalDateTime.now());

		// 用户信息插入，同时github的也插入
		try {
			userMapper.insertUser(user);
			githubUserMapper.insert(githubUser);
		} catch (SQLException sqlException) {
			log.error("用户信息插入失败，错误信息：{}", sqlException.getMessage());
			ExceptionUtil.putException(map, ExceptionEnum.REGISTER_HAD_USER);
			return;
		} catch (Exception e) {
			log.error("用户信息插入失败，错误信息：{}", e.getMessage());
			ExceptionUtil.putException(map, ExceptionEnum.SYSTEM_EXCEPTION);
			return;
		}

		User temp = userMapper.selectByGithubId(githubUser.getId());
		ExceptionUtil.putException(map, ExceptionEnum.COMMON_SUCCESS);
		map.put("url", INDEX);
		map.put("data", temp);
	}

	/**
	 * 通过code获取访问用户信息的 token
	 * @param code
	 * @return
	 */
	private String getAccessTokenByCode(String code) {

		// 拼接能获取 accessToken 的url
		String baseAccessTokenUrl = "https://github.com/login/oauth/access_token" +
				"?client_id=%s" +
				"&client_secret=%s" +
				"&code=%s" +
				"&redirect_uri=%s";
		String accessTokenUrl = String.format(baseAccessTokenUrl, clientId, clientSecret, code, redirectUrl);
		// 请求 accessToken
		String params = HttpUtil.post(accessTokenUrl, (String) null);
		String accessToken = null;
		try {
			Map<String, String> paramMap = checkAndGetAccessTokenParams(params);
			accessToken = paramMap.get("access_token");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return accessToken;
	}

	/**
	 * 检查并格式化参数，封装在 map 中
	 * @param params
	 * @return
	 */
	private Map<String, String> checkAndGetAccessTokenParams(String params) {
		log.info("GithubUserServiceImpl 获取的accessToken参数为：{}", params);
		HashMap<String, String> map = new HashMap<>();
		try {
			String[] parts = params.split("&");
			if (parts.length != 3) {
				throw new IllegalArgumentException();
			}
			for (String part : parts) {
				String[] p = part.split("=");
				if (p.length != 2) {
					throw new IllegalArgumentException();
				}
				map.put(p[0], p[1]);
			}
		} catch (IllegalArgumentException e) {
			log.error("GithubUserServiceImpl ：checkAndGetAccessTokenParams 参数异常");
			throw new BusinessException("GithubUserServiceImpl ：checkAndGetAccessTokenParams 参数异常");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
}




