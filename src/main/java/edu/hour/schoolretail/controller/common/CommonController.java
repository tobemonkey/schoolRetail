package edu.hour.schoolretail.controller.common;

import edu.hour.schoolretail.common.constant.enums.exception.ExceptionEnum;
import edu.hour.schoolretail.service.BannerService;
import edu.hour.schoolretail.service.ProductService;
import edu.hour.schoolretail.service.UserService;
import edu.hour.schoolretail.util.CookieUtil;
import edu.hour.schoolretail.vo.shop.BannerVO;
import edu.hour.schoolretail.vo.shop.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月10日 10:49
 */
@RestController
@Slf4j
public class CommonController {

	@Resource
	private ProductService productService;

	@Resource
	private BannerService bannerService;

	@Resource
	private UserService userService;

	@GetMapping
	public ModelAndView getIndex(HttpServletRequest request, Model model) {

		Map<String, String> map = (Map<String, String>) request.getAttribute("data");
		// 用户信息
		if (map != null) {
			// 能够成功获取 id 则说明已经通过验证
			Long id = Long.valueOf(map.get("id"));
			addSimpleUserInfoToModel(id, model);
		}
		// 首页轮播图
		List<BannerVO> broadcasts = bannerService.selectAllBroadcast();
		model.addAttribute("broadcasts", broadcasts);
		// 今日推荐
		List<BannerVO> recommends = bannerService.selectAllRecommend();
		model.addAttribute("recommends", recommends);
		// 首页商品展示
		Map<String, List<ProductVO>> productMap = productService.selectAllCategoryGoods();
		model.addAttribute("productMap", productMap);
		return new ModelAndView("index");
	}


	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> data = (Map<String, String>) request.getAttribute("data");
		Long id = Long.valueOf(data.get("id"));
		log.info("用户 id 为：{} 退出！", id);
		userService.logout(id);
		// 删除对应的 cookie
		response.addCookie(CookieUtil.deleteCookie("token"));
		return null;
	}

	/**
	 * 将简单用户信息添加到 model
	 * @param id
	 * @param model
	 * @return
	 */
	public Map<String, Object> addSimpleUserInfoToModel(Long id, Model model) {
		Map<String, Object> simpleShowInfo = userService.selectSimpleShowInfo(id);
		// 如果查询成功则将数据封装给 model
		if (simpleShowInfo.get("status").equals(ExceptionEnum.COMMON_SUCCESS.getStatus())) {
			model.addAttribute("userSimpleInfo", simpleShowInfo.get("msg"));
		}
		return simpleShowInfo;
	}

}
