package edu.hour.schoolretail.mapper;

import edu.hour.schoolretail.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hour.schoolretail.vo.shop.ProductDetailVO;
import edu.hour.schoolretail.vo.shop.ProductVO;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
* @author demoy
* @description 针对表【t_product(商品信息)】的数据库操作Mapper
* @createDate 2023-03-22 21:02:55
* @Entity edu.hour.schoolretail.entity.Product
*/
public interface ProductMapper extends BaseMapper<Product> {

	/**
	 * 查找所有的商品以及对应商品的种类，并且按照商品种类作为key，商品作为value的方式进行返回
	 * @return
	 */
	@MapKey("categoryName")
	Map<String, List<ProductVO>> selectAllCategoryGoods();

	/**
	 * 通过商品 id 返回商品信息
	 * @param id
	 * @return
	 */
	ProductDetailVO selectGoodsInfo(Integer id);
}




