package edu.hour.schoolretail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * 用户微信基本信息
 * @TableName t_wechat_user
 */
@TableName(value ="t_wechat_user")
@Data
public class WechatUser implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 2354895209481114988L;
    /**
     * 用户唯一标识
     */
    @Id
    @TableId(value = "wechat_open_id")
    private String openId;

    /**
     * 用户昵称
     */
    @TableField(value = "wechat_nickname")
    private String nickname;

    /**
     * 用户性别
     */
    @TableField(value = "wechat_sex")
    private String sex;

    /**
     * 用户所在省份
     */
    @TableField(value = "wechat_province")
    private String province;

    /**
     * 用户所在城市
     */
    @TableField(value = "wechat_city")
    private String city;

    /**
     * 用户所在国家
     */
    @TableField(value = "wechat_country")
    private String country;

    /**
     * 用户头像url
     */
    @TableField(value = "wechat_head_image_url")
    private String headImageUrl;


    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", openId=" + openId +
                ", nickname=" + nickname +
                ", sex=" + sex +
                ", province=" + province +
                ", city=" + city +
                ", country=" + country +
                ", headImageUrl=" + headImageUrl +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}