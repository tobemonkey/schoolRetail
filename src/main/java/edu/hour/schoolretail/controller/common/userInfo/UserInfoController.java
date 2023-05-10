package edu.hour.schoolretail.controller.common.userInfo;

import edu.hour.schoolretail.common.constant.RedisKeys;
import edu.hour.schoolretail.common.constant.Role;
import edu.hour.schoolretail.common.validated.loginAndRegister.BindEmailGroup;
import edu.hour.schoolretail.controller.common.CommonController;
import edu.hour.schoolretail.dto.LoginAndRegisterDto;
import edu.hour.schoolretail.dto.Result;
import edu.hour.schoolretail.dto.UserDetailInfoDTO;
import edu.hour.schoolretail.service.OrderService;
import edu.hour.schoolretail.service.ShopcarService;
import edu.hour.schoolretail.service.UserService;
import edu.hour.schoolretail.util.*;
import edu.hour.schoolretail.vo.shop.ShopcarVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月16日 09:00
 */
@RestController
@RequestMapping("/userInfo")
@Slf4j
public class UserInfoController {
	private static final Long MAX_IMAGE_SIZE = (long) (5 * 1024 * 1024);

	@Resource
	private EmailUtil emailUtil;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Resource
	private UserService userService;

	@Resource
	private ShopcarService shopcarService;

	@Resource
	private CommonController commonController;

	@GetMapping("/show")
	public ModelAndView showUserInfo(HttpServletRequest request, Model model) {
		Map<String, String> data = (Map<String, String>) request.getAttribute("data");
		Long id = Long.valueOf(data.get("id"));
		log.info("查询用户 id 为 {} 的用户信息", id);

		// 添加建议用户信息
		commonController.addSimpleUserInfoToModel(id, model);
		// 查询用户详细信息
		Map<String, Object> detailInfo = userService.selectDetailUserInfo(id);
		model.addAttribute("data", detailInfo.get("data"));
		return new ModelAndView("common/user/userInfo/userInfo");
	}

	@GetMapping("/rebindEmail")
	public ModelAndView rebindEmail(HttpServletRequest request) {
		Map<String, String> data = (Map<String, String>) request.getAttribute("data");
		Long id = Long.valueOf(data.get("id"));
		log.info("用户 id 为 {} 的用户申请重新绑定邮箱", id);
		return new ModelAndView("common/user/userInfo/bindEmail");
	}

	@Transactional
	@PostMapping("/rebindEmail")
	public Result rebindEmail(@Validated(BindEmailGroup.class) @RequestBody LoginAndRegisterDto loginAndRegisterDto, BindingResult result, HttpServletRequest request) {
		// 参数获取
		Map<String, String> data = (Map<String, String>) request.getAttribute("data");
		Long id = Long.valueOf(data.get("id"));
		log.info("用户详细信息为：{}，id 为：{}", loginAndRegisterDto, id);
		if (result.hasErrors()) {
			log.info("用户详细信息填写异常，异常信息为：{}", result.getFieldError().getDefaultMessage());
			return Result.failForUser(result.getFieldError().getDefaultMessage());
		}

		Map<String, String> map = userService.rebindEmail(loginAndRegisterDto, id);
		String status = map.get("status");
		String msg = map.get("msg");
		if (status.equals("0000")) {
			return Result.success("操作成功");
		} else if (status.startsWith("1")) {
			return Result.failForUser(msg);
		} else {
			return Result.failForSystem(msg);
		}
	}

	@Transactional
	@PostMapping("/submit")
	public Result submitUserInfo(@Valid UserDetailInfoDTO userDetailInfoDTO, BindingResult result, HttpServletRequest request) {

		// 参数获取
		Map<String, String> data = (Map<String, String>) request.getAttribute("data");
		Long id = Long.valueOf(data.get("id"));
		log.info("用户详细信息为：{}，id 为：{}", userDetailInfoDTO, id);
		if (result.hasErrors()) {
			log.info("用户详细信息填写异常，异常信息为：{}", result.getFieldError().getDefaultMessage());
			return Result.failForUser(result.getFieldError().getDefaultMessage());
		}
		if (userDetailInfoDTO.getImage() != null) {
			long fileSize = userDetailInfoDTO.getImage().getSize();
			if (fileSize > MAX_IMAGE_SIZE) {
				log.info("用户头像大效果大，大小为：{} MB", fileSize / 1024.0 / 1024.0);
				return Result.failForUser("用户头像大小超过了 5MB");
			}
		}

		Map<String,String> map = userService.updateUserInfo(userDetailInfoDTO, id);
		String status = map.get("status");
		String msg = map.get("msg");
		if (status.equals("0000")) {
			return Result.success("操作成功");
		} else if (status.startsWith("1")) {
			return Result.failForUser(msg);
		} else {
			return Result.failForSystem(msg);
		}
	}

	@GetMapping("/forget")
	public ModelAndView getForgetPage() {
		return new ModelAndView("common/user/userInfo/forget");
	}

	@GetMapping({"/forget/verify/{email}", "/bindEmail/verify/{email}"})
	public Result getVerifyCOde(@PathVariable("email") String email, HttpServletRequest request) {
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
		if (request.getRequestURI().contains("forget")) {
			redisTemplate.opsForValue().set(RedisKeys.KEY_FORGET_VERIFY + email, verifyCode);
		} else if (request.getRequestURI().contains("bindEmail")) {
			redisTemplate.opsForValue().set(RedisKeys.KEY_BIND_EMAIL + email, verifyCode);
		}

		return Result.success("验证码已发送");
	}

	@PostMapping("/forget/resetPassword")
	public Result resetPassword(@Valid @RequestBody LoginAndRegisterDto loginAndRegisterDto, BindingResult result) {
		// 参数校验
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

	@GetMapping("/shopcar")
	public ModelAndView getShopCar(HttpServletRequest request, Model model) {
		// 参数获取
		Map<String, String> data = (Map<String, String>) request.getAttribute("data");
		Long id = Long.valueOf(data.get("id"));
		log.info("查询购物车信息的用户 id 为：{}", id);

		// 添加简洁用户信息
		commonController.addSimpleUserInfoToModel(id, model);
		// 购物车信息查询
		List<ShopcarVO> shopcarVOS = shopcarService.selectUserShopcar(id);
		model.addAttribute("data", shopcarVOS);
		return new ModelAndView("common/user/userInfo/shopcar");
	}

	@GetMapping("/shopcar/delete/{shopcarId}")
	public Result deleteShopcarInfo(@PathVariable("shopcarId") Integer shopcarId, HttpServletRequest request) {
		if (shopcarId == null) {
			return Result.failForUser("待删除的购物车信息 id 为空");
		}
		// 参数获取
		Map<String, String> data = (Map<String, String>) request.getAttribute("data");
		Long id = Long.valueOf(data.get("id"));

		if (shopcarService.deleteShopcarInfo(id, shopcarId) == 1) {
			return Result.success("删除成功");
		}
		return Result.failForUser("删除失败");

	}

	@PostMapping("/changeRole")
	public Result changeRole(HttpServletRequest request, HttpServletResponse response) {
		// 参数获取
		Map<String, String> data = (Map<String, String>) request.getAttribute("data");
		Long id = Long.valueOf(data.get("id"));

		Integer identity = userService.changeRole(id);
		if (identity != null) {
			String token = JWTUtil.createToken(id, identity);
			response.addCookie(CookieUtil.getCookie("token", token));
			return Result.success(null);
		} else {
			return Result.failForUser("修改失败");
		}
	}

}
