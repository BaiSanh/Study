package com.baisanh.config;

import com.baisanh.interceptor.JwtTokenAdminInterceptor;
import com.baisanh.interceptor.JwtTokenUserInterceptor;
import com.baisanh.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 * 注册自定义拦截器
 * 设置静态资源映射
 * 设置knife4j接口文档
 * 扩展消息转换器
 */

@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        //管理端拦截器，拦截 /admin/**路径，但排除登录路径
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        //用户端拦截器，拦截 /user/**路径，但排除登录路径和店铺状态
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/user/browserLogin")
                .excludePathPatterns("/user/shop/status");
    }

    /**
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("点餐系统项目接口文档")
                .version("2.0")
                .description("点餐系统项目接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.baisanh.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * 设置静态资源映射
     * 这些配置使得前端可以直接访问这些静态资源文件，例如HTML页面、JavaScript文件、CSS文件等
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");

        // 配置管理端图片映射
        registry.addResourceHandler("/admin/common/images/**")
                .addResourceLocations("file:D:/IDEA_Project/Capstone/uploads/image/");

        // 配置用户端图片映射
        registry.addResourceHandler("/user/common/images/**")
                .addResourceLocations("file:D:/IDEA_Project/Capstone/userUpload/");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    /**
     * 扩展消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        //创建一个新的 MappingJackson2HttpMessageConverter 对象，该对象用于将Java对象转换为JSON格式的数据，反之亦然
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //使用自定义的 JacksonObjectMapper 替换默认的对象映射器
        converter.setObjectMapper(new JacksonObjectMapper());
        //将自定义的转换器添加到转换器列表的最前面，以覆盖默认的转换器
        converters.add(0,converter);
    }

    //TODO: 跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8888")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
