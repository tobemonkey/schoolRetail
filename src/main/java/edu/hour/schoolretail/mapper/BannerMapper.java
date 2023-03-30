package edu.hour.schoolretail.mapper;

import edu.hour.schoolretail.entity.Banner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hour.schoolretail.vo.shop.BannerVO;

import java.util.List;

/**
* @author demoy
* @description 针对表【t_banner(轮播图和今日推荐)】的数据库操作Mapper
* @createDate 2023-03-25 21:01:20
* @Entity edu.hour.schoolretail.entity.Banner
*/
public interface BannerMapper extends BaseMapper<Banner> {

	/**
	 * 查询所有状态为1的轮播图
	 * @return
	 */
	List<BannerVO> selectAllBroadcast();

	List<BannerVO> selectAllRecommend();
}




