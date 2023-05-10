package edu.hour.schoolretail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hour.schoolretail.common.constant.RedisKeys;
import edu.hour.schoolretail.common.constant.enums.exception.ExceptionEnum;
import edu.hour.schoolretail.controller.common.AddressController;
import edu.hour.schoolretail.dto.LoginAndRegisterDto;
import edu.hour.schoolretail.dto.ResultStateEnum;
import edu.hour.schoolretail.dto.UserDetailInfoDTO;
import edu.hour.schoolretail.entity.User;
import edu.hour.schoolretail.exception.LoginAndRegisterException;
import edu.hour.schoolretail.exception.UserOperationException;
import edu.hour.schoolretail.mapper.UserMapper;
import edu.hour.schoolretail.service.UserService;
import edu.hour.schoolretail.util.*;
import edu.hour.schoolretail.util.constant.FilePathConstant;
import edu.hour.schoolretail.vo.user.UserDetailInfoVO;
import edu.hour.schoolretail.vo.user.UserSimpleShowVO;
import edu.hour.schoolretail.websocket.WebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
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

	@Resource
	private AddressController addressController;


	@Override
	public Map<String, String> login(HttpServletRequest request, String email, String password, HttpServletResponse response) throws Exception {

		Map<String, String> result = new HashMap<>();
		// 获取基本登入信息
		Map<String, String> map = (Map<String, String>) request.getAttribute("data");
		// 如果携带了 token 就进行 token 验证登入
		User user = null;

		try {

			if (map != null && map.containsKey("id")) {
				Long id = Long.valueOf(map.get("id"));
				log.info("cookie 登入：id 为：{}， email为：{}", id, email);
				user = userMapper.loginByToken(id, email);
			} else {
				password = MD5Utils.generateMD5(password);
				log.info("密码登入：email为：{}， 密码为：{}", email, password);
				user = userMapper.loginByPassword(email, password);
			}
		} catch (SQLException sqlException) {
			log.error("登入阶段 SQL 执行失败，错误信息：{}", sqlException.getMessage());
			result.put("status", ResultStateEnum.SYSTEM_ERROR.getStateCode());
			result.put("msg", ResultStateEnum.SYSTEM_ERROR.getMsg());
			return result;
		}

		// 对用户信息进行判断
		if (user == null) {
			log.info("email 为 {} 的用户不存在", email);
			result.put("status", ResultStateEnum.USERNAME_OR_PASSWORD_ERROR.getStateCode());
			result.put("msg", ResultStateEnum.USERNAME_OR_PASSWORD_ERROR.getMsg());
			return result;
		}

		// 用户身份不符合
		if (!(user.getIdentity() == 1 || user.getIdentity() == 2)) {
			log.info("email 为 {} 的用户身份不符合登入条件", email);
			result.put("status", ResultStateEnum.ROLE_NOT_RIGHT.getStateCode());
			result.put("msg", ResultStateEnum.ROLE_NOT_RIGHT.getMsg());
			return result;
		}

		Long userId = user.getId();
		// 登入成功
		// 如果存在该用户，往结果中存入新token
		String token = getToken(userId, user.getIdentity());
		// 设置 cookie
		Cookie cookie = CookieUtil.getCookie("token", token);
		response.addCookie(cookie);

		// 用户状态设置
		changeOnline(ONLINE, userId);
		result.put("status", ResultStateEnum.SUCCESS.getStateCode());
		result.put("msg", ResultStateEnum.SUCCESS.getMsg());

		// 登入成功，信息存储
		return result;
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
			user.setUpdateTime(LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			addUserHeadImg(user);
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

	/**
	 * 将图片进行处理并且存储在本地，并且把路径放在数据库中
	 * @return
	 */
	private void addUserHeadImg(User user) {
		String imgPath = "/images/user/defaultHead.png";
		user.setImg(imgPath);
	}
	@Override
	public Map<String, String> resetPassword(String email, String password) {
		Map<String, String> res = new HashMap<>();
		log.info("修改前的用户信息：邮箱：{}，密码：{}", email, password);
		if (!userMapper.resetPassword(email, password)) {
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
	public void logout(Long id) {
		changeOnline(OFFLINE, id);
	}

	@Override
	public Map<String, Object> selectDetailUserInfo(Long id) {
		Map<String, Object> res = new HashMap<>();
		UserDetailInfoVO info = userMapper.selectUserDetailInfo(id);
		log.info("用户信息为：{}", info);
		if (info == null || info.getImage() == null) {
			ExceptionUtil.putException(res, ExceptionEnum.USER_NOT_EXISTS);
			return res;
		}
		String address = info.getAddress();
		if (address != null) {
			info.setAddresses(splitAddress(address));
			paddingAddressCode(info);
		}
		res.put("status", ExceptionEnum.COMMON_SUCCESS.getStatus());
		res.put("data", info);
		return  res;
	}

	@Override
	public Map<String, String> updateUserInfo(UserDetailInfoDTO userDetailInfoDTO, Long id) {
		Map<String, String> res = new HashMap<>();

		// 补充用户数据
		User user = userDetailInfoDTO.toUser();
		user.setUpdateTime(LocalDateTime.now());
		user.setId(id);
		// 商品图片处理
		if (userDetailInfoDTO.getImage() != null) {
			MultipartFile img = userDetailInfoDTO.getImage();
			String imgPath = imageProcess(img, String.valueOf(id));
			user.setImg(imgPath);
		}

		// 数据更新
		try {
			userMapper.updateById(user);
		} catch (Exception e) {
			log.error("用户信息插入失败，错误信息：{}", e.getMessage());
			res.put("status", ExceptionEnum.OTHERS_EXCEPTION.getStatus());
			res.put("msg", ExceptionEnum.OTHERS_EXCEPTION.getMsg());
			return res;
		}
		res.put("status", ExceptionEnum.COMMON_SUCCESS.getStatus());
		res.put("msg", "修改成功");
		return res;
	}

	@Override
	public Map<String, String> rebindEmail(LoginAndRegisterDto loginAndRegisterDto, Long id) {
		Map<String, String> res = new HashMap<>();
		if (checkVerifyCode(loginAndRegisterDto.getEmail(), loginAndRegisterDto.getVerifyCode(), RedisKeys.KEY_BIND_EMAIL)) {
			boolean success = false;
			try {
				success = userMapper.rebindUserEmail(loginAndRegisterDto.getEmail(), id);
			} catch (DuplicateKeyException e) {
				log.info("id 为 {} 用户想绑定的邮箱 {} 已被绑定", id, loginAndRegisterDto.getEmail());
				res.put("status", ExceptionEnum.EMAIL_HAD_REBIND.getStatus());
				res.put("msg", ExceptionEnum.EMAIL_HAD_REBIND.getMsg());
				return res;
			} catch (Exception e) {
				log.error("id 为 {} 的用户邮箱绑定失败，失败原因为：{}", id, e.getMessage());
				res.put("status", ExceptionEnum.OTHERS_EXCEPTION.getStatus());
				res.put("msg", e.getMessage());
				return res;
			}
			if (success) {
				res.put("status", ExceptionEnum.COMMON_SUCCESS.getStatus());
				res.put("msg", "修改成功");
			}
		}
		res.put("status", ExceptionEnum.COMMON_VERIFY_ERROR.getStatus());
		res.put("msg", ExceptionEnum.COMMON_VERIFY_ERROR.getMsg());
		return res;
	}

	@Override
	public Integer changeRole(Long id) {
		log.info("用户 id 为 {} 的用户申请修改身份", id);
		if (userMapper.updateUserRole(id)) {
			Integer identity = userMapper.selectIdentity(id);
			log.info("id 为 {} 的用户修改后的身份为：{}", id, identity);
			return identity;
		}

		return userMapper.selectIdentity(id);
	}

	public boolean checkVerifyCode(String email, String verifyCode, String type) {
		String cacheCode = (String) redisTemplate.opsForValue().get(type + email);
		if (cacheCode == null) {
			return false;
		}
		return cacheCode.equals(verifyCode);
	}

	/**
	 * 获取转义后的地址，并转化成四部分保存
	 * @param address
	 * @return
	 */
	private String[] splitAddress(String address) {
		String[] addresses = new String[4];
		char[] chars = address.toCharArray();
		int index = 0, temp = 0;
		for (int i = 0; i < chars.length && index < 3; i++) {
			if (chars[i] == '\\') {
				addresses[index] = address.substring(temp, i);
				temp = i + 1;
				index ++;
			}
		}
		addresses[index] = address.substring(temp);
		return addresses;
	}

	/**
	 * 填充用户地址的 code 属性，便于后续查询
	 * @param detailInfo
	 */
	private void paddingAddressCode(UserDetailInfoVO detailInfo) {
		String[] addresses = detailInfo.getAddresses();
		String[] code = new String[2];
		code[0] = addressController.selectCode(addresses[0]);
		code[1] = addressController.selectCode(addresses[1]);
		detailInfo.setAddressCode(code);
	}

	/**
	 * 将图片进行处理并且存储在本地，并且把路径放在数据库中
	 * @param img
	 * @param id 用户 id，因为唯一性，故直接用作图片名称
	 * @return
	 */
	private String imageProcess(MultipartFile img, String id) {
		String imgName = img.getOriginalFilename();
		String extension = imgName.substring(imgName.lastIndexOf("."));
		String imgPath = ImageUtil.getUserImageAbsolutePath(extension, FilePathConstant.USER_IMAGE, id);
		try {
			img.transferTo(new File(imgPath));
		} catch (IOException e) {
			log.error("商品图片存储异常！异常信息为：{}", e.getMessage());
			throw new UserOperationException(e.getMessage());
		}
		return imgPath.substring(imgPath.indexOf("images"));
	}
}




