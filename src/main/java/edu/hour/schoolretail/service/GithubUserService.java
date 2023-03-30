package edu.hour.schoolretail.service;

import edu.hour.schoolretail.dto.LoginAndRegisterDto;
import edu.hour.schoolretail.entity.GithubUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author demoy
* @description 针对表【t_github_user(github 认证用户的信息)】的数据库操作Service
* @createDate 2023-03-10 16:12:09
*/
public interface GithubUserService extends IService<GithubUser> {

	/**
	 * github 用户验证后调用的回调函数，一般用来保存用户的信息
	 * @param code
	 * @return
	 */
	Map<String, String> callback(String code);

	/**
	 * 发送邮件
	 * @return
	 */
	void sendVerifyCode(String target);

	/**
	 * 用于提交用户信息并登入
	 * @param token
	 * @param loginAndRegisterDto
	 * @return
	 */
	Map<String, Object> commit(String token, LoginAndRegisterDto loginAndRegisterDto);
}
