package edu.hour.schoolretail.vo.shop;

import java.math.BigDecimal;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月25日 14:31
 */
public class ProductVO {

	/**
	 * 商品id
	 */
	private Integer id;

	/**
	 * 商品名称
	 */
	private String name;


	/**
	 * 商品展示图
	 */
	private String image;


	/**
	 * 商品价格
	 */
	private BigDecimal price;

	public ProductVO() {
	}

	public ProductVO(Integer id, String name, String image, BigDecimal price) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.price = price;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
