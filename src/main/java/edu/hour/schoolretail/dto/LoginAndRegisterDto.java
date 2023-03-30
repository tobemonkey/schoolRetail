package edu.hour.schoolretail.dto;

import edu.hour.schoolretail.common.validated.loginAndRegister.LoginByAuthGroup;
import edu.hour.schoolretail.common.validated.loginAndRegister.LoginByPasswordGroup;
import edu.hour.schoolretail.common.validated.loginAndRegister.LoginByTokenGroup;
import edu.hour.schoolretail.common.validated.loginAndRegister.RegisterGroup;
import edu.hour.schoolretail.entity.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Author 201926002057 戴毅
 * @Description 登入注册时使用的数据传输对象
 * @Date 2023/2/2
 **/
@Data
public class LoginAndRegisterDto {

    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{10,16}$";

    /**
     * 密码
     */
    @Pattern(regexp = PASSWORD_PATTERN, message = "密码不合法", groups = {LoginByPasswordGroup.class, RegisterGroup.class})
    private String password;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式错误，请检查", groups = {LoginByPasswordGroup.class, LoginByTokenGroup.class, LoginByAuthGroup.class, RegisterGroup.class})
    @NotBlank(message = "邮箱不能为空", groups = {LoginByPasswordGroup.class, LoginByTokenGroup.class, LoginByAuthGroup.class, RegisterGroup.class})
    private String email;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空", groups = {LoginByAuthGroup.class, RegisterGroup.class})
    private String verifyCode;


    /**
     * 对两个映射对下个进行转化
     * @return
     */
    public User toUser() {
        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        return user;
    }


    /**
     * 将 user 转换成 LoginAndRegisterDto
     * @param user
     * @return
     */
    public static LoginAndRegisterDto fromUser(User user) {
        LoginAndRegisterDto loginAndRegisterDto = new LoginAndRegisterDto();
        loginAndRegisterDto.setPassword(user.getPassword());
        loginAndRegisterDto.setEmail(user.getEmail());
        return loginAndRegisterDto;
    }

}
