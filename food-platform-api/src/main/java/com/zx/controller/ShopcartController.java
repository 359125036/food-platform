package com.zx.controller;

import com.zx.pojo.bo.ShopcartBO;
import com.zx.utils.JSONResult;
import com.zx.utils.JsonUtils;
import com.zx.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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
public class ShopcartController extends BaseController{

    @Autowired
    private RedisOperator redisOperator;

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
        //前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        //判断购物车中是否已包含存在的商品，如果存在则数量累加
        String shopCartLJson=redisOperator.get(FOODIE_SHOPCART+":"+userId);
        List<ShopcartBO> list=new ArrayList<>();
        if(StringUtils.isNotBlank(shopCartLJson)){
            //redis中存在购物车
            list= JsonUtils.jsonToList(shopCartLJson,ShopcartBO.class);
            //判断购物车中是否已存在数量，如果有counts累加
            boolean isHaving=false;
            for (ShopcartBO bo : list) {
                //获取redis中商品规格id
                String specId=bo.getSpecId();
                //比对缓存与当前规格
                if(specId.equals(shopcartBO.getSpecId())){
                    //添加数量
                    bo.setBuyCounts(bo.getBuyCounts()+shopcartBO.getBuyCounts());
                    isHaving=true;
                }
            }
            //当redis缓存中不存在时添加
            if(!isHaving){
                list.add(shopcartBO);
            }
        }else{
            //不存在直接加入redis
            list.add(shopcartBO);
        }
        //覆盖现有redis数据
        redisOperator.set(FOODIE_SHOPCART+":"+userId,JsonUtils.objectToJson(list));

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

        //用户在页面删除购物车中的商品信息，如果用户已登录还需删除redis购物车中的信息
        String shopCartJson=redisOperator.get(FOODIE_SHOPCART+":"+userId);
        //redis有购物车数据
        if(StringUtils.isNotBlank(shopCartJson)){
            List<ShopcartBO> list=JsonUtils.jsonToList(shopCartJson,ShopcartBO.class);
            for (ShopcartBO shopcartBO : list) {
                if(shopcartBO.getSpecId().equals(itemSpecId)){
                    //当redis中存在该规格的商品时，将其删除并跳出循环
                    list.remove(shopcartBO);
                    break;
                }
            }
            //更新redis
            redisOperator.set(FOODIE_SHOPCART+":"+userId,JsonUtils.objectToJson(list));
        }
        return JSONResult.ok();
    }
}
