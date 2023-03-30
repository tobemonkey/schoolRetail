package edu.hour.schoolretail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * github 认证用户的信息
 * @TableName t_github_user
 */
@TableName(value ="t_github_user")
@Data
public class GithubUser implements Serializable {
    /**
     * github 用户的id
     */
    @TableId(value = "github_id")
    private Integer id;

    /**
     * github 用户对应的本地用户的id
     */
    @TableField(value = "github_user_id")
    private Long userId;

    /**
     * github 用户的用户名
     */
    @TableField(value = "github_login")
    private String login;

    /**
     * github 用户的头像地址
     */
    @TableField(value = "github_avatar_url")
    private String avatarUrl;

    /**
     * github 用户绑定的邮箱
     */
    @TableField(value = "github_email")
    private String email;

    /**
     * github 用户签名
     */
    @TableField(value = "github_bio")
    private String bio;

    @TableField(exist = false)
    private static final long serialVersionUID = -7664178433775306716L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        GithubUser other = (GithubUser) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getLogin() == null ? other.getLogin() == null : this.getLogin().equals(other.getLogin()))
            && (this.getAvatarUrl() == null ? other.getAvatarUrl() == null : this.getAvatarUrl().equals(other.getAvatarUrl()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getBio() == null ? other.getBio() == null : this.getBio().equals(other.getBio()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getLogin() == null) ? 0 : getLogin().hashCode());
        result = prime * result + ((getAvatarUrl() == null) ? 0 : getAvatarUrl().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getBio() == null) ? 0 : getBio().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", login=").append(login);
        sb.append(", avatarUrl=").append(avatarUrl);
        sb.append(", email=").append(email);
        sb.append(", bio=").append(bio);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}