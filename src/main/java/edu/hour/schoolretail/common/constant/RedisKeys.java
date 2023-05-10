package edu.hour.schoolretail.common.constant;

/**
 * @author demoy
 * @version 1.0.0
 * @Description
 * @createTime 2023年03月13日 09:51
 */
public class RedisKeys {

	/**
	 * 注册验证码 key 的前缀
	 */
	public static final String KEY_REGISTER_VERIFY = "REGISTER:VERIFY:";

	/**
	 * 登录验证码 key 的前缀
	 */
	public static final String KEY_LOGIN_VERIFY = "LOGIN:VERIFY:";

	/**
	 * 忘记密码验证码前缀
	 */
	public static final String KEY_FORGET_VERIFY = "FORGET:VERIFY:";

	/**
	 * github 验证前缀
	 */
	public static final String KEY_GITHUB_AUTH = "AUTH:GITHUB:VERIFY:";

	/**
	 * 用户绑定邮箱
	 */
	public static final String KEY_BIND_EMAIL = "BIND:VERIFY:";

	/**
	 * 解决超卖问题的乐观锁
	 */
	public static final String KEY_OVERSOLD = "OVERSOLD:";

	/**
	 * 消息插入数据库的锁
	 */
	public static final String KEY_MQ_INSERT = "MQ:INSERT:";

	/**
	 * 消息消费锁
	 */
	public static final String KEY_MQ_CONSUME = "MQ:CONSUME:";

	/**
	 * 确认订单锁
	 */
	public static final String KEY_CONFIRM_ORDER = "ORDER:CONFIRM:";

	/**
	 * 手动取消订单锁
	 */
	public static final String KEY_CANCEL_ORDER = "ORDER:CANCEL:";

}
