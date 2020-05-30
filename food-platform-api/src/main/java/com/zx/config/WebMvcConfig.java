package com.zx.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName: WebMvcConfig
 * @Author: zhengxin
 * @Description: 将RestTemplate初始化为bean
 * @Date: 2020/5/23 17:48
 * @Version: 1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * @Method addResourceHandlers
     * @Author zhengxin
     * @Description 映射资源到项目路径，实现网页访问
     * @param registry
     * @Return void
     * @Exception
     * @Date 2020/5/30 8:33
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")//所有路径
                .addResourceLocations("classpath:/META-INF/resources/")//映射swagger2
                .addResourceLocations("file:/workspaces/images/");//映射本地静态资源
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }
}
