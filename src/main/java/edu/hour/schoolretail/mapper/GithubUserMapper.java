package edu.hour.schoolretail.mapper;

import edu.hour.schoolretail.entity.GithubUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author demoy
* @description 针对表【t_github_user(github 认证用户的信息)】的数据库操作Mapper
* @createDate 2023-03-10 16:12:09
* @Entity edu.hour.schoolretail.entity.GithubUser
*/
@Mapper
public interface GithubUserMapper extends BaseMapper<GithubUser> {

}




