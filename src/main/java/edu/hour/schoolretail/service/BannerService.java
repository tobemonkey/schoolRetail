package edu.hour.schoolretail.service;

import edu.hour.schoolretail.entity.Banner;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hour.schoolretail.vo.shop.BannerVO;

import java.util.List;

/**
* @author demoy
* @description 针对表【t_banner(轮播图和今日推荐)】的数据库操作Service
* @createDate 2023-03-25 21:01:20
*/
public interface BannerService extends IService<Banner> {

	List<BannerVO> selectAllBroadcast();

	List<BannerVO> selectAllRecommend();
}
