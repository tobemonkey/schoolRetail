package edu.hour.schoolretail.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import edu.hour.schoolretail.interceptor.cookie.CookieInterceptor;
import edu.hour.schoolretail.interceptor.cookie.LightCookieInterceptor;
import edu.hour.schoolretail.interceptor.jwt.JWTInterceptor;
import edu.hour.schoolretail.interceptor.jwt.LightJWTInterceptor;
import edu.hour.schoolretail.interceptor.role.MerchantInterceptor;
import edu.hour.schoolretail.interceptor.role.UserInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Author 201926002057 戴毅
 * @Description
 * @Date 2023/2/2
 **/
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JWTInterceptor jwtInterceptor;

    @Resource
    private LightJWTInterceptor lightJWTInterceptor;

    @Resource
    private UserInterceptor userInterceptor;

    @Resource
    private MerchantInterceptor merchantInterceptor;

    @Resource
    private CookieInterceptor cookieInterceptor;

    @Resource
    private LightCookieInterceptor lightCookieInterceptor;

    /**
     * 对于不同的请求进行拦截，并进行一些处理，拦截器数字越小优先级越高，得大于0
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        /**
         * 针对必须要有 cookie 的页面
         */
        registry.addInterceptor(cookieInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/loginAndRegister/**", "/shop/goods/info/**", "/")
                .excludePathPatterns("/static/**")
                // 忘记密码
                .excludePathPatterns("/userInfo/forget/**")
                // 图片
                .excludePathPatterns("/images/**")
                // github 授权
                .excludePathPatterns("/login/oauth2/**")
                .order(9);


        /**
         * 针对一些页面可以有 cookie 也可以没有
         */
        registry.addInterceptor(lightCookieInterceptor)
                // 登入页面
                .addPathPatterns("/loginAndRegister/login")
                // 商品信息页面
                .addPathPatterns("/shop/goods/info/**")
                // 主页面
                .addPathPatterns("/")
                .order(9);

        // 登入拦截器，判断是否使用token直接登入
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/loginAndRegister/login")
//                .order(9);

        // 权限验证拦截器，判断当前访问用户是否合法
//        registry.addInterceptor(jwtInterceptor)
                // 登入，因为需要记住密码功能
//                .addPathPatterns("/loginAndRegister/login")
                // 商家的所有操作
//                .addPathPatterns("/merchant/**")
//                .addPathPatterns("/**")
//                // 除了登入功能
//                .excludePathPatterns("/loginAndRegister/verify")
//                .excludePathPatterns("/loginAndRegister/register")
//                .excludePathPatterns("/loginAndRegister/index")
//                .excludePathPatterns("/loginAndRegister")
//                // 静态资源
//                .excludePathPatterns("/static/**")
//                // 所有的认证登入
//                .excludePathPatterns("/login/**")
//                // 图标
//                .excludePathPatterns("/favicon.ico")
//                .excludePathPatterns("/")
//                .order(10);

        // 权限验证拦截器，判断当前用户身份是否满足访问条件
        registry.addInterceptor(merchantInterceptor)
                .addPathPatterns("/merchant/**")
                .order(11);
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/shop")
                .order(11);

    }


    /**
     * 资源映射处理器
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/images/**", "/shop/goods/info/images/**", "/userInfo/images/**", "/order/images/**", "/merchant/images/**")
                .addResourceLocations("file:F:/dshop/images/");

        WebMvcConfigurer.super.addResourceHandlers(registry);
    }


    /**
     * 跨域资源处理器
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // SpringBoot2.4.0 [allowedOriginPatterns]代替[allowedOrigins]
                .allowedMethods("*")
                .maxAge(3600)
                .allowCredentials(true);
    }


}
