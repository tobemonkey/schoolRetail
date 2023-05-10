package edu.hour.schoolretail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hour.schoolretail.entity.Banner;
import edu.hour.schoolretail.service.BannerService;
import edu.hour.schoolretail.mapper.BannerMapper;
import edu.hour.schoolretail.vo.shop.BannerVO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author demoy
* @description 针对表【t_banner(轮播图和今日推荐)】的数据库操作Service实现
* @createDate 2023-03-25 21:01:20
*/
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner>
    implements BannerService{

	@Resource
	private BannerMapper bannerMapper;

	/**
	 * 查询所有的轮播图图片
	 * @return
	 */
	@Override
	public List<BannerVO> selectAllBroadcast() {
		return bannerMapper.selectAllBroadcast();
	}

	@Override
	public List<BannerVO> selectAllRecommend() {
		return bannerMapper.selectAllRecommend();
	}
}




