package edu.hour.schoolretail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hour.schoolretail.entity.WechatUser;
import edu.hour.schoolretail.service.WechatUserService;
import edu.hour.schoolretail.mapper.WechatUserMapper;
import org.springframework.stereotype.Service;

/**
* @author demoy
* @description 针对表【t_wechat_user(用户微信基本信息)】的数据库操作Service实现
* @createDate 2023-03-07 23:45:24
*/
@Service
public class WechatUserServiceImpl extends ServiceImpl<WechatUserMapper, WechatUser>
    implements WechatUserService{

}




