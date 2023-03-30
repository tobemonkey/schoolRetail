package edu.hour.schoolretail.controller.common.userInfo;

import edu.hour.schoolretail.common.constant.RedisKeys;
import edu.hour.schoolretail.dto.LoginAndRegisterDto;
import edu.hour.schoolretail.dto.Result;
import edu.hour.schoolretail.service.UserService;
import edu.hour.schoolretail.util.EmailUtil;
import edu.hour.schoolretail.util.ExceptionUtil;
import edu.hour.schoolretail.util.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月16日 09:00
 */
@RestController
@RequestMapping("/common/userInfo")
@Slf4j
public class UserInfoController {

	@Resource
	private EmailUtil emailUtil;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Resource
	private UserService userService;

	@GetMapping("/forget")
	public ModelAndView getForgetPage() {
		return new ModelAndView("common/user/forget");
	}

	@GetMapping("/forget/verify/{email}")
	public Result getForgetVerify(@PathVariable("email") String email) {
		if (email == null || !emailUtil.isLegalEmail(email)) {
			log.warn("邮箱格式有误！请重新检查，email：{}", email);
			return Result.failForUser("邮箱格式有误！请重新检查");
		}
		// 获取验证码并发送邮件
		String verifyCode = null;
		try {
			verifyCode = emailUtil.sendVerifyCode(email);
			log.info("{} 的验证码为：{}", email, verifyCode);
		} catch (MessagingException e) {
			log.error("验证码发送错误");
			return Result.failForSystem("系统出现故障，请稍后重试");
		}
		redisTemplate.opsForValue().set(RedisKeys.KEY_FORGET_VERIFY + email, verifyCode);

		return Result.success("验证码已发送");
	}

	@PostMapping("/forget/resetPassword")
	public Result resetPassword(@Valid @RequestBody LoginAndRegisterDto loginAndRegisterDto, BindingResult result) {

		if (result.hasErrors()) {
			return Result.failForUser(result.getFieldError().getDefaultMessage());
		}

		String password = loginAndRegisterDto.getPassword();
		loginAndRegisterDto.setPassword(MD5Utils.generateMD5(password));
		log.info("传入的信息为：{}", loginAndRegisterDto);

		String email = loginAndRegisterDto.getEmail();
		String verifyCode = loginAndRegisterDto.getVerifyCode();
		String redisCode = (String) redisTemplate.opsForValue().get(RedisKeys.KEY_FORGET_VERIFY + loginAndRegisterDto.getEmail());
		if (redisCode == null) {
			return Result.failForUser("验证码失效了哦，请重新发送！");
		}
		if (!redisCode.equals(verifyCode)) {
			return Result.failForUser("验证码错误，请重新输入");
		}

		Map<String, String> map = userService.resetPassword(email, loginAndRegisterDto.getPassword());
		return ExceptionUtil.getResultMap(map);
	}
}
