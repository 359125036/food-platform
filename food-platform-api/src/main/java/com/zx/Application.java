package com.zx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName: InitApplication
 * @Description: 启动类
 * @Author: zhengxin
 * @Date: 2019/12/6 11:44
 * @Version: 1.0
 */

@SpringBootApplication
//扫描mybatis通用所在的包
@MapperScan(basePackages = "com.zx.mapper")
// 扫描所有包以及相关组件包
@ComponentScan(basePackages = {"com.zx", "org.n3r.idworker"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
