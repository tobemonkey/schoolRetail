package edu.hour.schoolretail.service;

import edu.hour.schoolretail.dto.shop.ProductDTO;
import edu.hour.schoolretail.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hour.schoolretail.vo.shop.ProductVO;

import java.util.List;
import java.util.Map;

/**
* @author demoy
* @description 针对表【t_product(商品信息)】的数据库操作Service
* @createDate 2023-03-22 21:02:55
*/
public interface ProductService extends IService<Product> {

	Map<String, Object> insertNewProduct(ProductDTO product);

	Map<String, List<ProductVO>> selectAllCategoryGoods();

	Map<String, Object> selectGoodsInfo(Integer id);
}
