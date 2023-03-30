package edu.hour.schoolretail.vo.shop;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @author demoy
 * @version 1.0.0
 * @Description 轮播图和今日推荐的VO
 * @createTime 2023年03月25日 20:42
 */
public class BannerVO {

	/**
	 * 图片地址
	 */
	private String image;

	/**
	 * 图片显示靠前级别，数字越小越靠前
	 */
	private Integer order;

	/**
	 * 图片对应的商品id
	 */
	private Integer goodsId;

	public BannerVO() {
	}

	public BannerVO(String image, Integer order, Integer goodsId) {
		this.image = image;
		this.order = order;
		this.goodsId = goodsId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
}
