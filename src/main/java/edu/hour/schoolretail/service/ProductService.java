package edu.hour.schoolretail.service;

import edu.hour.schoolretail.dto.shop.ProductDTO;
import edu.hour.schoolretail.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hour.schoolretail.vo.order.OrderVO;
import edu.hour.schoolretail.vo.shop.ProductVO;

import java.util.List;
import java.util.Map;

/**
* @author demoy
* @description 针对表【t_product(商品信息)】的数据库操作Service
* @createDate 2023-03-22 21:02:55
*/
public interface ProductService extends IService<Product> {

	/**
	 * 插入新商品的信息
	 * @param product
	 * @return
	 */
	Map<String, Object> insertNewProduct(ProductDTO product, Long id);

	/**
	 * 查询所有的商品
	 * @return
	 */
	Map<String, List<ProductVO>> selectAllCategoryGoods();

	/**
	 * 查询对应商品的信息
	 * @param id
	 * @return
	 */
	Map<String, Object> selectGoodsInfo(Integer id);

	/**
	 * 查询商品单价
	 * @param goodsId
	 * @return
	 */
	Map<String, String> selectGoodsPrice(Integer goodsId);

	/**
	 * 补全 orderVO 的信息
	 * @param orderVO
	 * @return
	 */
	Map<String, String> makeupOrderVO(OrderVO orderVO);
}
