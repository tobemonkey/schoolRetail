package edu.hour.schoolretail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.deploy.net.proxy.pac.PACFunctions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

/**
 * 用户信息表
 * @TableName t_user
 */
@TableName(value ="t_user")
@Data
@ApiModel("用户信息类")
public class User implements Serializable {

    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{10,16}$";

    /**
     * 用户id
     */
    @TableId(value = "user_id")
    @ApiModelProperty("用户id")
    private Long id;


    /**
     * 用户密码
     */
    @TableField(value = "user_password")
    @ApiModelProperty("用户密码")
    @Pattern(regexp = PASSWORD_PATTERN)
    private String password;

    /**
     * 用户邮箱
     */
    @TableField(value = "user_email")
    @ApiModelProperty("用户邮箱")
    @Email
    private String email;

    /**
     * 用户昵称
     */
    @TableField(value = "user_nickname")
    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * 用户签名
     */
    @TableField(value = "user_sign")
    @ApiModelProperty("用户签名")
    private String sign;

    /**
     * 用户收货地址
     */
    @TableField(value = "user_address")
    @ApiModelProperty("用户收货地址")
    private String address;

    /**
     * 用户头像存储地址
     */
    @TableField(value = "user_img")
    @ApiModelProperty("用户头像存储地址")
    private String img;

    /**
     * 用户创建时间
     */
    @TableField(value = "user_create_time")
    @ApiModelProperty("用户创建时间")
    // 作用是接收从页面传到后台的日期值
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    // 作用是从后台向前台传递日期值
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 用户身份标识，标识用户与管理员；也可以用于标识用户状态，比如冻结或正常
     */
    @TableField(value = "user_identity")
    @ApiModelProperty("用户标识身份")
    private Integer identity;

    /**
     * 用户在线标识：0表示下线，1表示在线
     */
    @TableField(value = "user_online")
    @ApiModelProperty("用户在线标识")
    @Max(1)
    private Integer online;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", password=").append(password);
        sb.append(", email=").append(email);
        sb.append(", nickname=").append(nickname);
        sb.append(", sign=").append(sign);
        sb.append(", address=").append(address);
        sb.append(", img=").append(img);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}