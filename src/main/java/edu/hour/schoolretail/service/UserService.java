package edu.hour.schoolretail.service;

import edu.hour.schoolretail.dto.LoginAndRegisterDto;
import edu.hour.schoolretail.dto.UserDetailInfoDTO;
import edu.hour.schoolretail.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author admin
 * @description 针对表【t_user(用户信息表)】的数据库操作Service
 * @createDate 2023-01-09 20:04:43
 */
public interface UserService extends IService<User> {

	/**
	 * 通过传入信息进行账号登入并返回 token
	 * @param request 请求体
	 * @param email 可以是邮箱也可以是用户名
	 * @param password 密码
	 *
	 * @return 封装了登入操作的操作结果
	 * @exception Exception 抛出异常触发 aop，从而触发事务回滚
	 */
	Map<String, String> login(HttpServletRequest request, String email, String password, HttpServletResponse response) throws Exception;


	/**
	 * 发送验证码验证身份
	 * @param target
	 */
	void sendVerifyCode(String target);

	/**
	 * 检测验证码
	 * @param email
	 * @param verifyCode
	 */
	boolean checkVerifyCode(String email, String verifyCode, String type);

	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
	boolean register(User user) throws Exception;

	/**
	 * 重置密码
	 * @param email
	 * @param password
	 * @return
	 */
	Map<String, String> resetPassword(String email, String password);

	/**
	 * 查询在页面中展示的简单用户信息
	 * @return
	 * @param id 用于查询用
	 */
	Map<String, Object> selectSimpleShowInfo(Long id);

	/**
	 * 用户登出
	 * @param request
	 */
	void logout(Long request);

	/**
	 * 查询用户详细信息
	 * @param id
	 * @return
	 */
	Map<String, Object> selectDetailUserInfo(Long id);

	/**
	 * 保存用户详细信息
	 * @param userDetailInfoDTO
	 * @param id
	 * @return
	 */
	Map<String, String> updateUserInfo(UserDetailInfoDTO userDetailInfoDTO, Long id);

	/**
	 * 绑定邮箱
	 * @param loginAndRegisterDto
	 * @param id
	 * @return
	 */
	Map<String, String> rebindEmail(LoginAndRegisterDto loginAndRegisterDto, Long id);

	/**
	 * 修改用户身份
	 * @param id
	 * @return
	 */
	Integer changeRole(Long id);
}
