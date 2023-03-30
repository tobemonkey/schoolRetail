package edu.hour.schoolretail.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hour.schoolretail.common.constant.RedisKeys;
import edu.hour.schoolretail.common.constant.enums.exception.ExceptionEnum;
import edu.hour.schoolretail.dto.ResultStateEnum;
import edu.hour.schoolretail.entity.User;
import edu.hour.schoolretail.exception.BusinessException;
import edu.hour.schoolretail.exception.LoginAndRegisterException;
import edu.hour.schoolretail.mapper.UserMapper;
import edu.hour.schoolretail.service.UserService;
import edu.hour.schoolretail.util.*;
import edu.hour.schoolretail.vo.UserSimpleShowVO;
import edu.hour.schoolretail.websocket.WebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author admin
 * @description 针对表【t_user(用户信息表)】的数据库操作Service实现
 * @createDate 2023-01-09 20:04:43
 */
@Service
@Slf4j
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
		implements UserService {

	// 用户名称初始化的前缀
	private static final String USER_NICKNAME_PREFIX = "用户";

	// 在线状态设置，0 表示在线， 1表示下线
	private static final Integer ONLINE = 1;
	private static final Integer OFFLINE = 0;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	@Resource
	private WebSocketHandler webSocketHandler;

	@Value("${mail.verify.expire}")
	private Long expire;

	@Resource
	private UserMapper userMapper;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Resource
	private EmailUtil emailUtil;


	@Override
	public Map<String, String> login(HttpServletRequest request, String email, String password) {

		Map<String, String> result = new HashMap<>();

		// 获取基本登入信息
		String token = request.getHeader("token");
		// 如果携带了 token 就进行 token 验证登入
		User user = null;

		try {
			if (token != null) {
				log.info("token 登入：token为：{}， email为：{}", token, email);
				user = loginByToken(token, email);
			} else {
				password = MD5Utils.generateMD5(password);
				log.info("密码登入：email为：{}， 密码为：{}", email, password);
				user = loginByPassword(email, password);
			}
		} catch (SQLException sqlException) {
			log.error("登入阶段 SQL 执行失败，错误信息：{}", sqlException.getMessage());
			result.put("status", ResultStateEnum.SYSTEM_ERROR.getStateCode());
			result.put("msg", ResultStateEnum.SYSTEM_ERROR.getMsg());
			return result;
		}

		// 对用户信息进行判断
		if (user == null) {
			result.put("status", ResultStateEnum.USERNAME_OR_PASSWORD_ERROR.getStateCode());
			result.put("msg", ResultStateEnum.USERNAME_OR_PASSWORD_ERROR.getMsg());
			return result;
		}

		Long userId = user.getId();
		// 如果存在该用户，往结果中存入新token
		result.put("token", getToken(userId, user.getIdentity()));

		// 登入成功
		changeOnline(ONLINE, userId);
		result.put("status", ResultStateEnum.SUCCESS.getStateCode());
		result.put("msg", ResultStateEnum.SUCCESS.getMsg());
		// 登入成功，信息存储
		return result;
	}

	/**
	 * 通过 token 登入并重新获取新 token
	 *
	 * @return
	 */
	private User loginByPassword(String email, String password) throws SQLException {
		User user = userMapper.loginByPassword(email, password);
		return user;
	}


	/**
	 * 通过 token 登入并重新获取新 token
	 *
	 * @param token
	 *
	 * @return
	 */
	private User loginByToken(String token, String email) throws SQLException {
		// 通过 token 解析 id
		Long id = decodeTokenAndGetId(token);
		// 通过 id 获取对应的用户信息
		User user = userMapper.loginByToken(id, email);
		return user;
	}

	/**
	 * 获取带有用户信息的 token
	 *
	 * @param id 用户唯一标识
	 *
	 * @return
	 */
	private String getToken(long id, Integer role) {
		// 获取带有用户身份信息的 token
		return JWTUtil.createToken(id, role);
	}

	/**
	 * 通过 token 解析出对应的 id
	 *
	 * @param token
	 *
	 * @return
	 */
	private Long decodeTokenAndGetId(String token) {
		long id;
		try {
			Map<String, String> claimsMap = JWTUtil.getClaimsMap(token);
			id = Long.parseLong(claimsMap.get("id"));
		} catch (Exception e) {
			log.error("token 解析错误！token 为：{}", token);
			throw new BusinessException("token 解析错误", e.getCause());
		}

		return id;
	}


	/**
	 * 修改在线状态
	 *
	 * @param status
	 */
	private void changeOnline(int status, Long id) {
		userMapper.changeOnline(status, id);
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
		} catch (MessagingException e) {
			log.error("邮件发送时出错！");
		}

		// 将验证码保存到 redis
		redisTemplate.opsForValue().set(RedisKeys.KEY_REGISTER_VERIFY + target, verifyCode, expire, TimeUnit.MINUTES);
		log.info("{} 的验证码为：{}", target, verifyCode);
	}

	@Override
	public boolean register(User user) throws DateTimeParseException, SQLException {
		// 默认信息设置
		long id = SnowFlakeUtil.nextId();
		user.setId(id);
		user.setNickname(USER_NICKNAME_PREFIX + id % 1000000);

		String time = null;
		try {
			// 密码加密
			user.setPassword(MD5Utils.generateMD5(user.getPassword()));
			// 设置创建时间
			long now = System.currentTimeMillis();
			time = dateFormat.format(now);
			user.setCreateTime(LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			user.setIdentity(1);
			// 插入数据
			if (userMapper.insertUser(user)) {
				return true;
			}
		} catch (SQLException e) {
			log.error("sql 执行异常：{}", e.getMessage());
			throw e;
		} catch (DuplicateKeyException e) {
			log.error("重复注册，邮箱账号为：{}", user.getEmail());
			throw e;
		} catch (DateTimeParseException e) {
			log.error("日期解析失败！时间格式为：{}，错误信息为：{}", time, e.getMessage());
		} catch (Exception e) {
			log.error("注册失败，失败信息：{}", e.getCause().getMessage());
			throw new LoginAndRegisterException(e.getMessage());
		}

		return false;
	}

	@Override
	public Map<String, String> resetPassword(String email, String password) {
		Map<String, String> res = new HashMap<>();
		log.info("修改前的用户信息：邮箱：{}，密码：{}", email, password);
		if (userMapper.resetPassword(email, password)) {
			ExceptionUtil.putException(res, ExceptionEnum.USER_NOT_EXISTS);
			return res;
		}
		ExceptionUtil.putException(res, ExceptionEnum.COMMON_SUCCESS);
		return  res;
	}

	@Override
	public Map<String, Object> selectSimpleShowInfo(Long id) {
		Map<String, Object> res = new HashMap<>();
		UserSimpleShowVO info = userMapper.selectSimpleShowInfo(id);
		if (info == null || info.getImage() == null) {
			ExceptionUtil.putException(res, ExceptionEnum.USER_NOT_EXISTS);
			return res;
		}
		res.put("status", ExceptionEnum.COMMON_SUCCESS.getStatus());
		res.put("msg", info);
		return  res;
	}

	@Override
	public boolean checkVerifyCode(String email, String verifyCode) {
		String cacheCode = (String) redisTemplate.opsForValue().get(RedisKeys.KEY_REGISTER_VERIFY + email);
		if (cacheCode == null) {
			return false;
		}
		return cacheCode.equals(verifyCode);
	}


}




