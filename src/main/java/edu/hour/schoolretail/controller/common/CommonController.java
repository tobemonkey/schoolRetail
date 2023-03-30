package edu.hour.schoolretail.controller.common;

import com.alibaba.fastjson.JSON;
import edu.hour.schoolretail.common.constant.enums.exception.ExceptionEnum;
import edu.hour.schoolretail.service.BannerService;
import edu.hour.schoolretail.service.ProductService;
import edu.hour.schoolretail.service.UserService;
import edu.hour.schoolretail.vo.shop.BannerVO;
import edu.hour.schoolretail.vo.shop.ProductVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月10日 10:49
 */
@Controller
public class CommonController {

	@Resource
	private ProductService productService;

	@Resource
	private BannerService bannerService;

	@Resource
	private UserService userService;

	@GetMapping
	public String getIndex() {
return null;
	}

	@GetMapping("/?")
	public String getIndex(HttpServletRequest request, Model model) {
		// 用户信息
		String token = request.getHeader("token");
		// token 不为空则说明 token 已经检验过了
		if (StringUtils.isNotBlank(token)) {
			String data = (String) request.getSession().getAttribute("data");
			HashMap<String, String> hashMap = JSON.parseObject(data, HashMap.class);
			Long id = Long.valueOf(hashMap.get("id"));
			Map<String, Object> simpleShowInfo = userService.selectSimpleShowInfo(id);
			// 如果查询成功则将数据封装给 model
			if (simpleShowInfo.get("status").equals(ExceptionEnum.COMMON_SUCCESS.getStatus())) {
				model.addAttribute("userInfo", simpleShowInfo.get("msg"));
			}
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
		return "index";
	}




}
