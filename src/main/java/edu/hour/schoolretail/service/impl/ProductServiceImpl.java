package edu.hour.schoolretail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hour.schoolretail.common.constant.enums.exception.ExceptionEnum;
import edu.hour.schoolretail.common.constant.enums.exception.MerchantExceptionEnum;
import edu.hour.schoolretail.dto.shop.ProductDTO;
import edu.hour.schoolretail.entity.Product;
import edu.hour.schoolretail.exception.UserOperationException;
import edu.hour.schoolretail.service.ProductService;
import edu.hour.schoolretail.mapper.ProductMapper;
import edu.hour.schoolretail.util.ImageUtil;
import edu.hour.schoolretail.util.JWTUtil;
import edu.hour.schoolretail.util.constant.FilePathConstant;
import edu.hour.schoolretail.vo.order.OrderVO;
import edu.hour.schoolretail.vo.shop.ProductDetailVO;
import edu.hour.schoolretail.vo.shop.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author demoy
* @description 针对表【t_product(商品信息)】的数据库操作Service实现
* @createDate 2023-03-22 21:02:55
*/
@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Resource
	private ProductMapper productMapper;



	@Override
	public Map<String, Object> insertNewProduct(ProductDTO productDTO, Long id) {

		Map<String, Object> res = new HashMap<>();

		// 补充商品数据
		Product product = productDTO.toProduct();
		product.setOwnerId(id);
		LocalDateTime now = LocalDateTime.now();
		product.setCreateTime(now);
		product.setUpdateTime(now);
		// 商品图片处理
		MultipartFile img = productDTO.getImg();
		String imgPath = imageProcess(img, product.getCategoryId());
		product.setImage(imgPath);

		// 数据插入
		try {
			productMapper.insert(product);
		} catch (Exception e) {
			log.error("商品信息插入失败，错误信息：{}", e.getMessage());
			putException(res, MerchantExceptionEnum.GOODS_INSERT_FAIL);
			return res;
		}
		putException(res, MerchantExceptionEnum.COMMON_SUCCESS);
		return res;
	}

	@Cacheable(value = "category")
	@Override
	public Map<String, List<ProductVO>> selectAllCategoryGoods() {
		return productMapper.selectAllCategoryGoods();
	}

	@Override
	public Map<String, Object> selectGoodsInfo(Integer id) {
		Map<String, Object> map = new HashMap<>();
		ProductDetailVO productDetailVO = productMapper.selectGoodsInfo(id);
		if (productDetailVO == null) {
			putException(map, MerchantExceptionEnum.GOODS_NOT_EXISTS);
		} else if (productDetailVO.getStatus() == 0) {
			putException(map, MerchantExceptionEnum.GOODS_HAVEN_FREEZE);
		} else {
			String address = productDetailVO.getMerchantAddress();
			StringBuilder sb = new StringBuilder();
			int i = 0, j = 0;
			for (; j < 3; i++) {
				if (address.charAt(i) == '\\') {
					j ++;
				} else {
					sb.append(address.charAt(i));
				}
			}
			sb.append(address.substring(i));
			productDetailVO.setMerchantAddress(sb.toString());
			putException(map, MerchantExceptionEnum.COMMON_SUCCESS);
			productDetailVO.setShopId(String.valueOf(productDetailVO.getMerchantId()));
			map.put("data", productDetailVO);
		}

		return map;
	}

	@Override
	public Map<String, String> selectGoodsPrice(Integer goodsId) {
		Map<String, String> res = new HashMap<>();
		BigDecimal price = null;
		try {
			price = productMapper.selectGoodsPrice(goodsId);
		} catch (Exception e) {
			log.error("查询商品单价出错，错误信息为：{}", e.getMessage());
			res.put("status", ExceptionEnum.SYSTEM_EXCEPTION.getStatus());
			res.put("msg", ExceptionEnum.SYSTEM_EXCEPTION.getMsg());
			return res;
		}
		if (price == null) {
			log.error("查询商品信息出错，查询 id 为 {}", goodsId);
			res.put("status", ExceptionEnum.GOODS_NOT_EXISTS.getStatus());
			res.put("msg", ExceptionEnum.GOODS_NOT_EXISTS.getMsg());
			return res;
		}

		res.put("status", ExceptionEnum.COMMON_SUCCESS.getStatus());
		res.put("msg", price.toString());
		return res;
	}

	@Override
	public Map<String, String> makeupOrderVO(OrderVO orderVO) {
		HashMap<String, String> map = new HashMap<>();
		log.info("补全商品 id 为 {} 的商家信息", orderVO.getGoodsId());
		// 主要考虑商品不存在问题
		OrderVO shopInfo = productMapper.makeUpShopInfo(orderVO.getGoodsId());
		if (shopInfo == null) {
			map.put("status", ExceptionEnum.GOODS_NOT_EXISTS.getStatus());
			map.put("msg", ExceptionEnum.GOODS_NOT_EXISTS.getMsg());
			return map;
		}

		orderVO.setMerchantId(shopInfo.getMerchantId());
		orderVO.setSendAddress(shopInfo.getSendAddress());
		// 返回成功
		map.put("status", ExceptionEnum.COMMON_SUCCESS.getStatus());
		return map;
	}

	/**
	 * 将图片进行处理并且存储在本地，并且把路径放在数据库中
	 * @param img
	 * @param id 商品种类id，用于拼接路径
	 * @return
	 */
	private String imageProcess(MultipartFile img, String id) {
		String imgName = img.getOriginalFilename();
		String extension = imgName.substring(imgName.lastIndexOf("."));
		String imgPath = ImageUtil.getPictureAbsolutePath(extension, FilePathConstant.GOODS_IMAGE, sdf.format(new Date()), id);
		try {
			img.transferTo(new File(imgPath));
		} catch (IOException e) {
			log.error("商品图片存储异常！异常信息为：{}", e.getMessage());
			throw new UserOperationException(e.getMessage());
		}
		return imgPath.substring(imgPath.indexOf("images"));
	}

	private void putException(Map<String, Object> map, MerchantExceptionEnum exception) {
		map.put("status", exception.getStatus());
		map.put("msg", exception.getMsg());
		map.put("url", exception.getUrl());
	}
}




