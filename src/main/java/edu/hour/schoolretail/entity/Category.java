package edu.hour.schoolretail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 商品种类表
 * @TableName t_category
 */
@TableName(value ="t_category")
@Data
public class Category implements Serializable {
    /**
     * 商品种类 id
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品种类名称
     */
    @TableField(value = "category_name")
    private String name;

    /**
     * 商品种类展示顺序
     */
    @TableField(value = "category_show_order")
    private Integer showOrder;

    /**
     * 该商品种类对应的图片
     */
    @TableField(value = "category_image")
    private String image;

    /**
     * 商品种类在前端定位的锚点id
     */
    @TableField(value = "category_anchor_id")
    private String anchorId;

    /**
     * 商品种类创建时间
     */
    @TableField(value = "category_create_time")
    private LocalDateTime createTime;

    /**
     * 商品种类修改时间
     */
    @TableField(value = "category_update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", showOrder=").append(showOrder);
        sb.append(", image=").append(image);
        sb.append(", anchorId=").append(anchorId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}