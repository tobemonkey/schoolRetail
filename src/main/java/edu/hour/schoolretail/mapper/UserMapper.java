package edu.hour.schoolretail.mapper;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import edu.hour.schoolretail.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hour.schoolretail.vo.user.UserDetailInfoVO;
import edu.hour.schoolretail.vo.user.UserSimpleShowVO;
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
	User loginByPassword(@Param("email") String email, @Param("password") String password) throws SQLException;


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
	void updatePassword(@Param("userId")Long userId);

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
	UserSimpleShowVO selectSimpleShowInfo(@Param("id") Long id);

	/**
	 * 查询用户详细信息
	 * @param id
	 * @return
	 */
	UserDetailInfoVO selectUserDetailInfo(@Param("id") Long id);

	/**
	 * 绑定邮箱
	 * @param email
	 * @param id
	 * @return
	 */
	boolean rebindUserEmail(@Param("email") String email, @Param("id") Long id);

	/**
	 * 查询该用户是否存在
	 * @param id
	 * @return
	 */
	boolean existsUser(@Param("id") Long id);

	/**
	 * 查询用户的收货地址
	 * @param buyerId
	 * @return
	 */
	String selectUserAddress(@Param("id") Long buyerId);

	/**
	 * 修改用户在线状态
	 */
	void updateOnlineStatus(@Param("status") Integer status, @Param("id") Long id);

	/**
	 * 修改用户身份
	 * @param id
	 * @return
	 */
	boolean updateUserRole(@Param("id") Long id);

	/**
	 * c查询用户身份
	 * @param id
	 * @return
	 */
	Integer selectIdentity(@Param("id") Long id);
}




