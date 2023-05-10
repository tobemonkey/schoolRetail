package edu.hour.schoolretail.mapper;

import edu.hour.schoolretail.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hour.schoolretail.vo.order.OrderVO;
import edu.hour.schoolretail.vo.shop.ProductDetailVO;
import edu.hour.schoolretail.vo.shop.ProductVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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
	ProductDetailVO selectGoodsInfo(@Param("id") Integer id);

	/**
	 * 查询商品单价
	 * @param goodsId
	 * @return
	 */
	BigDecimal selectGoodsPrice(@Param("goodsId") Integer goodsId);

	/**
	 * 查询订单商品的一些信息，包括：商品库存，商品单价
	 * @param goodsId
	 */
	Product selectGoodsOrderInfo(@Param("id") Integer goodsId);

	/**
	 * 查询商品剩余库存
	 * @param goodsId
	 * @return
	 */
	int selectGoodsStock(Integer goodsId);

	/**
	 * 删减商品库存
	 *
	 * @param goodsId
	 * @param goodsCount
	 * @return
	 */
	boolean reduceGoodsStock(@Param("goodsId") Integer goodsId, @Param("count") Integer goodsCount);

	/**
	 * 回退库存
	 * @param goodsId
	 * @param goodsNum
	 */
	void backStock(@Param("goodsId") Integer goodsId, @Param("goodsNum") Integer goodsNum);

	/**
	 * 查询商品名称
	 * @param goodsId
	 * @return
	 */
	String selectGoodsName(@Param("goodsId") Integer goodsId);

	/**
	 * 补全 orderVO 中的商家信息
	 * @param goodsId
	 * @return
	 */
	OrderVO makeUpShopInfo(@Param("goodsId") Integer goodsId);
}




