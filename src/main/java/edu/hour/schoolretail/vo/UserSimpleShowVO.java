package edu.hour.schoolretail.vo;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月10日 13:30
 */
public class UserSimpleShowVO {

	private String image;

	private Integer count;

	public UserSimpleShowVO() {
	}

	public UserSimpleShowVO(String image, Integer count) {
		this.image = image;
		this.count = count;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
