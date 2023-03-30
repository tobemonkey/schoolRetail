package edu.hour.schoolretail.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hour.schoolretail.entity.Category;
import edu.hour.schoolretail.service.CategoryService;
import edu.hour.schoolretail.mapper.CategoryMapper;
import edu.hour.schoolretail.vo.shop.CategoryMapVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author demoy
* @description 针对表【t_category(商品种类表)】的数据库操作Service实现
* @createDate 2023-03-23 11:37:12
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

	@Resource
	private CategoryMapper categoryMapper;

	/**
	 * 查询所有的商品种类 id
	 * @return
	 */
	@Override
	public List<CategoryMapVO> selectAllId() {

		List<CategoryMapVO> categoryMap = categoryMapper.selectAllId();
		return categoryMap;
	}
}




