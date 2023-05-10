package edu.hour.schoolretail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户信息表
 * @TableName t_user
 */
@TableName(value ="t_user")
@Data
public class User implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = -7161552747389270418L;
    /**
     * 用户id
     */
    @TableId(value = "user_id")
    private Long id;

    /**
     * 用户邮箱
     */
    @TableField(value = "user_email")
    private String email;

    /**
     * 用户密码
     */
    @TableField(value = "user_password")
    private String password;

    /**
     * 用户昵称
     */
    @TableField(value = "user_nickname")
    private String nickname;

    /**
     * 用户签名
     */
    @TableField(value = "user_sign")
    private String sign;

    /**
     * 用户地址
     */
    @TableField(value = "user_address")
    private String address;

    /**
     * 用户头像
     */
    @TableField(value = "user_img")
    private String img;

    /**
     * 用户身份标识，标识用户与管理员；也可以用于标识用户状态，比如冻结或正常
     */
    @TableField(value = "user_identity")
    private Integer identity;

    /**
     * 用户在线标识：0表示下线，1表示在线
     */
    @TableField(value = "user_online")
    private Integer online;

    /**
     * 用户创建时间
     */
    @TableField(value = "user_create_time")
    private LocalDateTime createTime;

    /**
     * 用户信息修改时间
     */
    @TableField(value = "user_update_time")
    private LocalDateTime updateTime;


}