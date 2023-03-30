package edu.hour.schoolretail.controller.shop;

import edu.hour.schoolretail.common.constant.enums.exception.MerchantExceptionEnum;
import edu.hour.schoolretail.service.ProductService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月25日 23:00
 */
@RestController
@RequestMapping("/shop/goods")
public class GoodsController {

	@Resource
	private ProductService productService;

	@GetMapping("/info/{id}")
	public ModelAndView getGoodsInfo(@PathVariable("id") Integer id, Model model, HttpSession session) {
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
}
