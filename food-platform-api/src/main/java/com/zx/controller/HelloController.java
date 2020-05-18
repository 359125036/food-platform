package com.zx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @ClassName: HelloController
 * @Description:
 * @Author: zhengxin
 * @Date: 2019/12/6 11:41
 * @Version: 1.0
 */
@ApiIgnore
@RestController//@RestController返回的都是json
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello world";
    }
}
