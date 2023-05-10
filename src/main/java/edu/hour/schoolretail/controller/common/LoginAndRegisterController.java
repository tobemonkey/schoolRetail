package edu.hour.schoolretail.controller.common;

import edu.hour.schoolretail.common.constant.RedisKeys;
import edu.hour.schoolretail.dto.LoginAndRegisterDto;
import edu.hour.schoolretail.dto.Result;
import edu.hour.schoolretail.entity.User;
import edu.hour.schoolretail.service.UserService;
import edu.hour.schoolretail.util.CookieUtil;
import edu.hour.schoolretail.util.EmailUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

/**
 * @Author 201926002057 戴毅
 * @Description
 * @Date 2023/1/9
 **/
@RestController
@RequestMapping("/loginAndRegister")
@Slf4j
@Api(value = "登入")
public class LoginAndRegisterController {

    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{6,16}$";

    @Autowired
    private UserService userService;

    @Autowired
    private EmailUtil emailUtil;

    @GetMapping
    public ModelAndView getLRPage() {
        return new ModelAndView("common/user/loginAndRegister");
    }

    @ApiOperation("用户登入")
    @PostMapping("/login")
    @Transactional
    public Result login(@RequestBody @Valid LoginAndRegisterDto loginAndRegisterDto, BindingResult bindingResult,
                        HttpServletRequest request, HttpServletResponse response) throws LoginException {
        // 参数校验，并且排除验证码的影响
        if (bindingResult.getErrorCount() > 1 && !bindingResult.getFieldErrors("verifyCode").isEmpty()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            log.error("登入时参数校验失败，错误信息为：{}", errorMsg);
            return Result.failForUser(errorMsg);
        }

        // 获取登入的操作结果
        Map<String, String> resultMap = null;
        try {
            resultMap = userService.login(request, loginAndRegisterDto.getEmail(), loginAndRegisterDto.getPassword(), response);
        } catch (Exception e) {
            log.error("登入阶段出现异常，异常信息为：{}", e.getMessage());
            return Result.failForSystem("系统出错，登入异常，请稍后再试");
        }
        return parseMapToResult(resultMap);
    }


    @ApiOperation("获取验证码")
    @GetMapping("/verify")
    public Result getVerifyCode(String email) {

        if (email == null || !emailUtil.isLegalEmail(email)) {
            log.warn("邮箱格式有误！请重新检查，email：{}", email);
            return Result.failForUser("邮箱格式有误！请重新检查");
        }
        // 获取验证码并发送邮件
        userService.sendVerifyCode(email);

        return Result.success(null);
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody @Valid LoginAndRegisterDto loginAndRegisterDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            log.error("注册时参数校验失败，错误信息为：{}", errorMsg);
            return Result.failForUser(errorMsg);
        }

        // 转换成 User，方便注册操作
        User user = loginAndRegisterDto.toUser();
        // 检测验证码是否符合
        if (!userService.checkVerifyCode(user.getEmail(), loginAndRegisterDto.getVerifyCode(), RedisKeys.KEY_REGISTER_VERIFY)) {
            return Result.failForUser("验证码超时或错误！");
        }

        try {
            if(userService.register(user)) {
                return Result.success("注册成功，去登入吧！");
            }
        } catch (DuplicateKeyException e) {

            return Result.failForUser("该邮箱已被使用！");
        } catch (Exception e) {

            e.printStackTrace();
        }

        return Result.failForSystem("注册失败，系统错误！");
    }


    /**
     * 解析传入的 map，并将其解析为 Result 返回
     * @param map
     * @return
     */
    public static Result parseMapToResult(Map<String, String> map) {

        String status = map.get("status");
        String token = map.get("token");
        String msg = map.get("msg");
        if (status.startsWith("0")) {
            return Result.success(token);
        } else if (status.startsWith("-2")) {
            return Result.failForUser(msg);
        } else if (status.startsWith("-1")) {
            return Result.failForSystem(msg);
        } else {
            log.warn("未记录状态，状态值为：{}，状态信息为：{}", status, msg);
            return Result.success(msg);
        }
    }

}
