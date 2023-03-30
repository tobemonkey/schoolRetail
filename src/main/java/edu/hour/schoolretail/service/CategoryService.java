package edu.hour.schoolretail.service;

import edu.hour.schoolretail.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hour.schoolretail.vo.shop.CategoryMapVO;

import java.util.List;

/**
* @author demoy
* @description 针对表【t_category(商品种类表)】的数据库操作Service
* @createDate 2023-03-23 11:37:12
*/
public interface CategoryService extends IService<Category> {

	List<CategoryMapVO> selectAllId();
}
