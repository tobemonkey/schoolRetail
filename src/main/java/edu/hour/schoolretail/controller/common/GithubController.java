package edu.hour.schoolretail.controller.common;

import edu.hour.schoolretail.common.constant.enums.exception.ExceptionEnum;
import edu.hour.schoolretail.common.validated.loginAndRegister.LoginByAuthGroup;
import edu.hour.schoolretail.dto.LoginAndRegisterDto;
import edu.hour.schoolretail.dto.Result;
import edu.hour.schoolretail.service.GithubUserService;
import edu.hour.schoolretail.util.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * @author demoy
 * @create 23:00
 */
@RestController
@RequestMapping("/login/oauth2/github")
@Slf4j
public class GithubController {

	@Resource
	private GithubUserService githubUserService;

	@Resource
	private EmailUtil emailUtil;

	/**
	 * github 用户认证后，会调用的 callback 函数，并去获取用户数据
	 * @param code
	 */
	@GetMapping("/callback")
	public Result callback(String code, HttpSession session, HttpServletResponse response) {
		log.info("获取 github 用户 code：{}", code);

		Map<String, String> map = githubUserService.callback(code, response);
		String url = map.get("url");
		// 如果没有对应的键，就返回 null
		session.setAttribute("data", map.get("data"));

		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			log.error("重定向失败！错误原因：{}", e.getMessage());
			return Result.failForSystem("系统错误，请联系管理员：18046863263");
		}

		return Result.success(null);

	}


	/**
	 * 返回绑定用户和用户的github的页面
	 * @return
	 */
	@GetMapping("/bind/{token}")
	public ModelAndView bind(@PathVariable("token") String token) {
		log.info("token 为 {} 的用户申请绑定", token);
		return new ModelAndView("common/user/auth/github");
	}


	@GetMapping("/verify/{email}")
	public Result getAuthVerify(@PathVariable("email") String email) {
		if (email == null || !emailUtil.isLegalEmail(email)) {
			log.warn("邮箱格式有误！请重新检查，email：{}", email);
			return Result.failForUser("邮箱格式有误！请重新检查");
		}
		// 获取验证码并发送邮件
		githubUserService.sendVerifyCode(email);
		return Result.success("验证码已发送");
	}

	@PostMapping("/commit/{token}")
	public Result commit(@PathVariable("token") String token,
	                     @Validated(LoginByAuthGroup.class) @RequestBody LoginAndRegisterDto loginAndRegisterDto,
	                     BindingResult result, HttpServletResponse response) {

		if (result.hasErrors()) {
			log.info("传入的信息为：{}", loginAndRegisterDto);
			return Result.failForUser(result.getFieldError().getDefaultMessage());
		}

		if (token == null || StringUtils.isBlank(token)) {
			log.error("传入的 token为:{}", token);
			return Result.failForSystem("系统出错，请联系管理员：18046863263");
		}

		Map<String, Object> commit = githubUserService.commit(token, loginAndRegisterDto, response);
		String status = (String) commit.get("status");
		if (status.equals(ExceptionEnum.COMMON_SUCCESS.getStatus())) {
			return Result.success(commit);
		} else if (status.startsWith("4") || status.startsWith("5")) {
			return Result.failForSystem(commit);
		}
		return Result.failForUser(commit);
 	}
}