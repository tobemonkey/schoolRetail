package edu.hour.schoolretail.redis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月14日 09:14
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisTest {

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	public void saveAndGet() {
		Person person = new Person("demoy", "男");
		redisTemplate.opsForValue().set("test", person, Duration.ofSeconds(10));
		Person p = (Person) redisTemplate.opsForValue().get("test");
		System.out.println(p);
		p = (Person) redisTemplate.opsForValue().get("test");
		System.out.println(p);
		redisTemplate.opsForValue().get("132");
	}
}
