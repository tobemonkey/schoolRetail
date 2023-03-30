package edu.hour.schoolretail.config;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Author 201926002057 戴毅
 * @Description 数据库连接池配置
 * @Date 2023/1/9
 **/
@Configuration
@Slf4j
public class JdbcConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String passwordCiphertext;

    @Value("${jdbc.publicKey}")
    private String publicKey;

    @Value("${druid.initialSize}")
    private Integer initialSize;

    @Value("${druid.maxActive}")
    private Integer maxActive;

    @Value("${druid.minIdle}")
    private Integer minIdle;

    @Value("${druid.maxWait}")
    private Long maxWait;

    @Value("${druid.timeBetweenEvictionRunsMillis}")
    private Long checkTime;

    @Bean(initMethod = "init")
    public DruidDataSource druidDataSource() {
        log.info("数据库连接池初始化开始");
        DruidDataSource druidDataSource = new DruidDataSource();
        String password;
        try {
            password = ConfigTools.decrypt(publicKey, passwordCiphertext);
        } catch (Exception e) {
            log.error("数据库连接池初始化失败，用户名密码解密失败");
            throw new RuntimeException(e);
        }
        log.info("数据库连接池配置初始化");
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        // 一些其他属性配置
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setInitialSize(initialSize);
        // 回收连接会话时最少留下多少个连接
        druidDataSource.setMinIdle(initialSize);
        // 最大连接时间
        druidDataSource.setMaxWait(maxWait);
        // 检测时间，如果空闲则可能被关闭
        druidDataSource.setTimeBetweenEvictionRunsMillis(checkTime);
        druidDataSource.setMinIdle(minIdle);
        log.info("数据库连接池初始化完成！");
        return druidDataSource;
    }
}
