package edu.hour.schoolretail.dto.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.hour.schoolretail.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author demoy
 * @version 1.0.0
 * @Description 前端交互数据的商品类
 * @createTime 2023年03月22日 20:24
 */
public class ProductDTO implements Serializable {


	private static final long serialVersionUID = -1419312843114147977L;

	@JsonProperty("token")
	@NotBlank(message = "token不能为空")
	private String token;

	@JsonProperty("category")
	@Pattern(regexp = "^[\\d]{3}$", message = "商品种类选择错误")
	private String category;

	@JsonProperty("name")
	@NotBlank(message = "商品名称不能为空")
	private String name;

	@JsonProperty("desc")
	@NotBlank(message = "商品描述不能为空")
	private String desc;

	@JsonProperty("stock")
	@Digits(fraction = 0, integer = 7, message = "库存非法，必须为整数，不限量请用负数，且库存量不能超过千万")
	private Integer stock;

	@JsonProperty("price")
	@Digits(integer = 7, fraction = 2, message = "价格非法，价格不能超过千万，且小数位数不能超过两位")
	private BigDecimal price;

	@JsonProperty("img")
	@NotNull(message = "商品图片不能为空")
	private MultipartFile img;



	public ProductDTO() {
	}

	public ProductDTO(@NotBlank(message = "token不能为空") String token, @Pattern(regexp = "^[\\d]{3}$", message = "商品种类选择错误") String category, @NotBlank(message = "商品名称不能为空") String name, @NotBlank(message = "商品描述不能为空") String desc, @Digits(fraction = 0, integer = 7, message = "库存非法，必须为整数，不限量请用负数，且库存量不能超过千万") Integer stock, @Digits(integer = 7, fraction = 2, message = "价格非法，价格不能超过千万，且小数位数不能超过两位") BigDecimal price, @NotNull(message = "商品图片不能为空") MultipartFile img) {
		this.token = token;
		this.category = category;
		this.name = name;
		this.desc = desc;
		this.stock = stock;
		this.price = price;
		this.img = img;
	}

	public Product toProduct() {
		Product product = new Product();
		product.setCategoryId(this.getCategory());
		product.setName(this.getName());
		product.setDesc(this.getDesc());
		product.setStock(this.getStock());
		product.setPrice(this.getPrice());
		return product;
	}


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public MultipartFile getImg() {
		return img;
	}

	public void setImg(MultipartFile img) {
		this.img = img;
	}
}
