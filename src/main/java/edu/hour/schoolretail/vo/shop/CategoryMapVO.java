package edu.hour.schoolretail.vo.shop;

/**
 * @author demoy
 * @version 1.0.0
 * @Description 商品种类与前端交互的对象
 * @createTime 2023年03月23日 14:22
 */
public class CategoryMapVO {

	private String id;

	private String name;

	private String anchorId;

	public CategoryMapVO() {
	}

	public CategoryMapVO(String id, String name, String anchorId) {
		this.id = id;
		this.name = name;
		this.anchorId = anchorId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAnchorId() {
		return anchorId;
	}

	public void setAnchorId(String anchorId) {
		this.anchorId = anchorId;
	}
}
