package edu.hour.schoolretail.config;

import edu.hour.schoolretail.interceptor.JWTInterceptor;
import edu.hour.schoolretail.interceptor.LightJWTInterceptor;
import edu.hour.schoolretail.interceptor.role.MerchantInterceptor;
import edu.hour.schoolretail.interceptor.role.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

/**
 * @Author 201926002057 戴毅
 * @Description
 * @Date 2023/2/2
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JWTInterceptor jwtInterceptor;

//    @Resource
//    private LoginInterceptor loginInterceptor;

    @Resource
    private LightJWTInterceptor lightJWTInterceptor;

    @Resource
    private UserInterceptor userInterceptor;

    @Resource
    private MerchantInterceptor merchantInterceptor;

    /**
     * 对于不同的请求进行拦截，并进行一些处理，拦截器数字越小优先级越高，得大于0
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(lightJWTInterceptor)
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
        registry.addInterceptor(jwtInterceptor)
                // 登入，因为需要记住密码功能
//                .addPathPatterns("/loginAndRegister/login")
                // 商家的所有操作
                .addPathPatterns("/merchant/**")
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
                .order(10);

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

        registry.addResourceHandler("/images/**", "/shop/goods/info/images/**")
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
