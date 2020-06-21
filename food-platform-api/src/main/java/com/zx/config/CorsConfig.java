package com.zx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @ClassName: CorsConfig
 * @Description: 跨域的配置
 * @Author: zhengxin
 * @Date: 2019/12/16 16:53
 * @Version: 1.0
 */
@Configuration
public class CorsConfig {
    public CorsConfig() {
    }
    @Bean
    public CorsFilter corsFilter() {
        //1.添加cors（跨域）配置
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:8080");
        corsConfiguration.addAllowedOrigin("http://shop.food.zhengxinwang.xyz:8080");
        corsConfiguration.addAllowedOrigin("http://center.food.zhengxinwang.xyz:8080");
        corsConfiguration.addAllowedOrigin("http://shop.food.zhengxinwang.xyz");
        corsConfiguration.addAllowedOrigin("http://center.food.zhengxinwang.xyz");
        corsConfiguration.addAllowedOrigin("http://47.115.41.69:8080");
        corsConfiguration.addAllowedOrigin("*");
        // 设置是否发送cookie信息
        corsConfiguration.setAllowCredentials(true);

        // 设置允许请求的方式
        corsConfiguration.addAllowedMethod("*");

        // 设置允许的header
        corsConfiguration.addAllowedHeader("*");

        // 2. 为url添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", corsConfiguration);

        // 3. 返回重新定义好的corsSource
        return new CorsFilter(corsSource);
    }
}
