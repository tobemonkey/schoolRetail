package edu.hour.schoolretail.redis;

import lombok.Data;

import java.io.Serializable;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月14日 09:15
 */
@Data
public class Person implements Serializable {

	private static final long serialVersionUID = 6453340880927424802L;
	private String name;

	private String sex;

	public Person() {
	}

	public Person(String name, String sex) {
		this.name = name;
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "Person{" +
				"name='" + name + '\'' +
				", sex='" + sex + '\'' +
				'}';
	}
}
