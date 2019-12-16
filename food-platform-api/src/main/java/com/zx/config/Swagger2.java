package com.zx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName: Swagger2
 * @Description: 生成api接口文档
 * @Author: zhengxin
 * @Date: 2019/12/16 9:21
 * @Version: 1.0
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    // http://localhost:8088/swagger-ui.html  swagger原网页
    //http://localhost:8088/doc.html          bootstrap渲染过的页面
    //配置swagger的核心配置docket

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  // 指定api类型为swagger2
                .apiInfo(apiInfo())                 // 用于定义api文档汇总信息
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.zx.controller"))   // 指定controller包
                .paths(PathSelectors.any())         // 所有controller
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                    .title("我不是在吃，就是在吃的路上 吃货平台接口api")  //文档页标题
                    //联系人信息
                    .contact(new Contact("zx","http://localhost:8088/","359125036@qq.com"))
                    //详细信息
                    .description("专为吃货平台提供的接口")
                    .version("1.0.1")//版本号
                    .termsOfServiceUrl("http://localhost:8088/")
                    .build();
    }

}
