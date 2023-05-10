package edu.hour.schoolretail.controller.user;

import edu.hour.schoolretail.common.constant.enums.exception.ExceptionEnum;
import edu.hour.schoolretail.dto.Result;
import edu.hour.schoolretail.service.ShopcarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月14日 18:45
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	@Resource
	private ShopcarService shopcarService;

	@PostMapping("/addShopcar")
	public Result addShopcar(@RequestParam("goodsId") Integer goodsId, @RequestParam("count") Integer count, HttpServletRequest request) {
		if (count < 1) {
			return Result.failForUser("商品数量不能低于 1");
		}

		Map<String, String> data = (Map<String, String>) request.getAttribute("data");
		Long userId = Long.valueOf(data.get("id"));
		log.info("添加购物车信息，用户ID：{}，商品ID：{}，数量：{}", userId, goodsId, count);

		Map<String, String> map = shopcarService.pushShopcarInfo(userId, goodsId, count);
		if (map.get("status").equals(ExceptionEnum.COMMON_SUCCESS.getStatus())) {
			return Result.success(null);
		}
		return Result.failForSystem(map.get("msg"));
	}
}
