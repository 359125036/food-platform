package com.zx.controller;

import com.zx.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: RedisController
 * @Author: zhengxin
 * @Description: 测试redis
 * @Date: 2020/8/26 15:26
 * @Version: 1.0
 */
@RestController
@RequestMapping("redis")
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public String set(String key,String value){
//        redisTemplate.opsForValue().set(key,value);
        redisOperator.set(key,value);
        return "OK";
    }

    @GetMapping("/get")
    public String get(String key){
//        return (String)redisTemplate.opsForValue().get(key);
        return redisOperator.get(key);
    }

    @GetMapping("/delete")
    public String delete(String key){
//        redisTemplate.delete(key);
        redisOperator.del(key);
        return "OK";
    }
}
