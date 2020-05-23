package com.zx.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName: WebMvcConfig
 * @Author: zhengxin
 * @Description: 将RestTemplate初始化为bean
 * @Date: 2020/5/23 17:48
 * @Version: 1.0
 */
@Configuration
public class WebMvcConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }
}
