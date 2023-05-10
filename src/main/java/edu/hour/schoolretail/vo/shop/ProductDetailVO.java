package edu.hour.schoolretail.vo.shop;

import java.math.BigDecimal;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月25日 23:05
 */
public class ProductDetailVO {

	/**
	 * 卖家 id
	 */
	private Long merchantId;

	private String shopId;
	/**
	 * 商品名称
	 */
	private String name;

	/**
	 * 商品简介
	 */
	private String desc;

	/**
	 * 商品展示图
	 */
	private String image;

	/**
	 * 商品库存
	 */
	private Integer stock;

	/**
	 * 商品价格
	 */
	private BigDecimal price;

	/**
	 * 商品状态
	 */
	private Integer status;

	/**
	 * 商家地址
	 */
	private String merchantAddress;

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMerchantAddress() {
		return merchantAddress;
	}

	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}
}
