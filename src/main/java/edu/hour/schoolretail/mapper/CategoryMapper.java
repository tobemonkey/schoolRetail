package edu.hour.schoolretail.mapper;

import edu.hour.schoolretail.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hour.schoolretail.vo.shop.CategoryMapVO;

import java.util.List;

/**
* @author demoy
* @description 针对表【t_category(商品种类表)】的数据库操作Mapper
* @createDate 2023-03-23 11:37:12
* @Entity edu.hour.schoolretail.entity.Category
*/
public interface CategoryMapper extends BaseMapper<Category> {

	List<CategoryMapVO> selectAllId();
}




