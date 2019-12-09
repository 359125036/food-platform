package com.zx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: HelloController
 * @Description: TODO
 * @Author: zhengxin
 * @Date: 2019/12/6 11:41
 * @Version: 1.0
 */
@RestController//@RestController返回的都是json
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello world";
    }
}
