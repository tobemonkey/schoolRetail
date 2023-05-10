package edu.hour.schoolretail.controller.shop;

import edu.hour.schoolretail.common.constant.enums.exception.ExceptionEnum;
import edu.hour.schoolretail.common.constant.enums.exception.MerchantExceptionEnum;
import edu.hour.schoolretail.controller.common.CommonController;
import edu.hour.schoolretail.dto.Result;
import edu.hour.schoolretail.entity.Product;
import edu.hour.schoolretail.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月25日 23:00
 */
@RestController
@RequestMapping("/shop/goods")
@Slf4j
public class GoodsController {

	@Resource
	private ProductService productService;

	@Resource
	private CommonController commonController;

	@GetMapping("/info/{id}")
	public ModelAndView getGoodsInfo(@PathVariable("id") Integer id, Model model, HttpSession session, HttpServletRequest request) {
		Map<String, String> data = (Map<String, String>) request.getAttribute("data");
		Long userId = Long.valueOf(data.getOrDefault("id", null));
		log.info("查询用户 id 为 {} 的用户信息", userId);

		// 添加建议用户信息
		commonController.addSimpleUserInfoToModel(userId, model);
		Map<String, Object> goodsInfo = productService.selectGoodsInfo(id);
		String status = (String) goodsInfo.get("status");
		// 如果查找成功则跳转对应页面
		if (status.equals(MerchantExceptionEnum.COMMON_SUCCESS.getStatus())) {
			model.addAttribute("goodsInfo", goodsInfo.get("data"));
			return new ModelAndView("shop/shopDetail");
		}
		// 否则将错误信息存在session中，前端进行显示
		session.setAttribute("data", goodsInfo);
		return null;
	}

	@GetMapping("/info/price/{goodsId}")
	public Result getGoodsPrice(@PathVariable("goodsId") Integer goodsId) {
		log.info("查询商品 id 为 {} 的单价", goodsId);
		Map<String, String> map = productService.selectGoodsPrice(goodsId);
		String status = map.get("status");
		String msg = map.get("msg");
		if (status.equals(ExceptionEnum.COMMON_SUCCESS.getStatus())) {
			return Result.success(msg);
		} else if (status.startsWith("2")) {
			return Result.failForUser(msg);
		}
		return Result.failForSystem(msg);
	}


}
