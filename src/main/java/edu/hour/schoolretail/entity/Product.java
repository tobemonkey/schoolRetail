package edu.hour.schoolretail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 商品信息
 * @TableName t_product
 */
@TableName(value ="t_product")
@Data
public class Product implements Serializable {
    /**
     * 商品id
     */
    @TableId(value = "product_id")
    private Integer id;

    /**
     * 商品名称
     */
    @TableField(value = "product_name")
    private String name;

    /**
     * 商品所属的类别
     */
    @TableField(value = "product_category_id")
    private String categoryId;

    /**
     * 商品所属商家的id
     */
    @TableField(value = "product_owner_id")
    private Long ownerId;

    /**
     * 商品简介
     */
    @TableField(value = "product_desc")
    private String desc;

    /**
     * 商品展示图
     */
    @TableField(value = "product_image")
    private String image;

    /**
     * 商品库存
     */
    @TableField(value = "product_stock")
    private Integer stock;

    /**
     * 商品价格
     */
    @TableField(value = "product_price")
    private BigDecimal price;

    /**
     * 商品上架状态
     */
    @TableField(value = "product_status")
    private Integer status;

    /**
     * 商品创建时间
     */
    @TableField(value = "product_create_time")
    private LocalDateTime createTime;

    /**
     * 商品修改时间
     */
    @TableField(value = "product_update_time")
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
        sb.append(", categoryId=").append(categoryId);
        sb.append(", ownerId=").append(ownerId);
        sb.append(", desc=").append(desc);
        sb.append(", image=").append(image);
        sb.append(", stock=").append(stock);
        sb.append(", price=").append(price);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}