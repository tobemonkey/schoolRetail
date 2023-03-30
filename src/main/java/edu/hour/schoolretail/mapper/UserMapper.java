package edu.hour.schoolretail.mapper;

import edu.hour.schoolretail.entity.GithubUser;
import edu.hour.schoolretail.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hour.schoolretail.vo.UserSimpleShowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;

/**
* @author admin
* @description 针对表【t_user(用户信息表)】的数据库操作Mapper
* @createDate 2023-01-09 20:04:43
* @Entity edu.hour.schoolretail.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 通过 email 进行登入
     * @param email 邮箱
     * @param password 密码
     * @return
     */
    User loginByPassword(@Param("email") String email, @Param("password") String password);


    /**
     * 注册用户
     * @param user
     * @return
     */
    boolean insertUser(@Param("user") User user) throws SQLException;

    /**
     * 设置在线状态
     * @param status
     */
    void changeOnline(@Param("status") Integer status, @Param("id") Long id);

    /**
     * 通过 id 进行登入
     * @param id 用户 id
     * @return
     */
    User loginByToken(@Param("id") Long id, @Param("email") String email);



    /**
     * 通过 github id 进行查询与之绑定的用户
     * @param id
     * @return
     */
    User selectByGithubId(@Param("githubId") Integer id);

    /**
     * 查看对应的邮箱是否已经被绑定了
     * @param email
     * @return
     */
	Long getUserIdByEmail(@Param("email") String email);

    /**
     * 更新用户密码
     * @param userId
     * @param password
     */
    void updatePassword(@Param("userId")Long userId, @Param("password")String password);

    /**
     * 更新用户密码
     * @param email
     * @param password
     * @return
     */
    boolean resetPassword(@Param("email") String email, @Param("password") String password);

    /**
     * 查询用于简易显示用户的信息
     * @param id
     * @return
     */
    UserSimpleShowVO selectSimpleShowInfo(Long id);
}




