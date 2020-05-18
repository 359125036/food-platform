package com.zx.controller;

import com.zx.pojo.bo.ShopcartBO;
import com.zx.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: ShopcartController
 * @Author: zhengxin
 * @Description: 购物车接口
 * @Date: 2020/5/17 17:36
 * @Version: 1.0
 */
@Api(value = "购物车接口",tags = {"购物车接口相关的api"})
@RestController
@RequestMapping("shopcart")
public class ShopcartController {

    /**
     * @Method add
     * @Author zhengxin
     * @Description 添加商品到购物车
     * @param userId 用户id
     * @param shopcartBO 添加商品信息
     * @param request
     * @param response
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/5/18 21:10
     */
    @ApiOperation(value = "添加商品到购物车",notes = "添加商品到购物车",httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(@RequestParam String userId,
                          @RequestBody ShopcartBO shopcartBO,
                          HttpServletRequest request,
                          HttpServletResponse response){
        //判断用户id是否为空
        if(StringUtils.isBlank(userId))
            return JSONResult.errorMsg(null);
        System.out.println(shopcartBO);
        //TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        return JSONResult.ok();
    }

    /**
     * @Method del
     * @Author zhengxin
     * @Description
     * @param userId 用户id
     * @param itemSpecId 商品规格id
     * @param request
     * @param response
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/5/18 21:16
     */
    @ApiOperation(value = "从购物车中删除商品",notes = "从购物车中删除商品",httpMethod = "POST")
    @PostMapping("/del")
    public JSONResult del(@RequestParam String userId,
                          @RequestParam String itemSpecId,
                          HttpServletRequest request,
                          HttpServletResponse response){
        //判断用户id是否为空
        if(StringUtils.isBlank(userId)&&StringUtils.isBlank(itemSpecId))
            return JSONResult.errorMsg("参数不能为空");

        //TODO 用户在页面删除购物车中的商品信息，如果用户已登录还需删除后端购物车中的信息
        return JSONResult.ok();
    }
}
