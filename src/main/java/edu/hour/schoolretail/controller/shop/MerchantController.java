package edu.hour.schoolretail.controller.shop;

import edu.hour.schoolretail.controller.common.CommonController;
import edu.hour.schoolretail.dto.Result;
import edu.hour.schoolretail.dto.shop.ProductDTO;
import edu.hour.schoolretail.service.CategoryService;
import edu.hour.schoolretail.service.ProductService;
import edu.hour.schoolretail.vo.shop.CategoryMapVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author demoy
 * @version 1.0.0
 * @Description 商家的有关控制器
 * @createTime 2023年03月22日 20:07
 */
@Slf4j
@RestController
@RequestMapping("/merchant")
public class MerchantController {
	private static final Long MAX_IMAGE_SIZE = (long) (10 * 1024 * 1024);

	@Resource
	private ProductService productService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private CommonController commonController;

	@GetMapping("/addProduct")
	public ModelAndView getAddProductPage(Model model, HttpServletRequest request) {
		// 参数获取
		Map<String, String> data = (Map<String, String>) request.getAttribute("data");
		Long id = Long.valueOf(data.get("id"));
		log.info("查询购物车信息的用户 id 为：{}", id);

		// 添加简洁用户信息
		commonController.addSimpleUserInfoToModel(id, model);
		List<CategoryMapVO> categoryMap = categoryService.selectAllId();
		model.addAttribute("categoryMap", categoryMap);
		return new ModelAndView("shoper/addProduct");
	}

	@PostMapping("/addProduct/submit")
	public Result submitProductInfo(@Valid ProductDTO productDTO, BindingResult result, HttpServletRequest request) {
		// 参数获取
		Map<String, String> data = (Map<String, String>) request.getAttribute("data");
		Long id = Long.valueOf(data.get("id"));
		log.info("查询购物车信息的用户 id 为：{}", id);
		// 参数判断
		log.info("商店申请信息如下：{}", productDTO);
		if (result.hasErrors()) {
			log.info("商品信息错误，错误信息为：{}", result.getFieldError().getDefaultMessage());
			return Result.failForUser(result.getFieldError().getDefaultMessage());
		}

		if (productDTO.getImg().getSize() > MAX_IMAGE_SIZE) {
			log.info("用户图片过大，大小为：{}", productDTO.getImg().getSize() / 1024 / 1024);
			return Result.failForUser("用户图片大小过大，应该在10MB以内");
		}

		// 数据入库
		Map<String, Object> map = productService.insertNewProduct(productDTO, id);
		String status = (String) map.get("status");
		String msg = (String) map.get("msg");
		if (status.equals("0000")) {
			return Result.success("操作成功");
		} else if (status.startsWith("1")) {
			return Result.failForUser(msg);
		} else {
			return Result.failForSystem(msg);
		}
	}

}
