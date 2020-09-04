package com.zx.controller;

import com.zx.enums.YesOrNoEnum;
import com.zx.pojo.Carousel;
import com.zx.pojo.Category;
import com.zx.pojo.vo.CategoryVO;
import com.zx.pojo.vo.NewItemsVO;
import com.zx.service.CarouselService;
import com.zx.service.CategoryService;
import com.zx.utils.JSONResult;
import com.zx.utils.JsonUtils;
import com.zx.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: IndexController
 * @Description: 首页展示的相关接口
 * @Author: zhengxin
 * @Date: 2019/12/24 11:07
 * @Version: 1.0
 */
@Api(value = "首页",tags = {"首页展示的相关接口"})
@RestController//@RestController返回的都是json
@RequestMapping("index")
public class IndexController {
    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisOperator redisOperator;

    /**
     * @Method carousel
     * @Author zhengxin
     * @Description 获取首页轮播图列表
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2019/12/24 11:22
     */
    @ApiOperation(value = "获取首页轮播图列表",notes = "获取首页轮播图列表",httpMethod = "GET")
    @GetMapping("/carousel")
    public JSONResult carousel(){
        List<Carousel> carouselList=new ArrayList<>();
        String carouselStr=redisOperator.get("carousel");
        //redis没有轮播图数据时，查询数据库
        if(StringUtils.isBlank(carouselStr)){
            //获取首页轮播图列表
            carouselList= carouselService.queryAllCarousel(YesOrNoEnum.YES.type);
            //将轮播图数据加入redis
            redisOperator.set("carousel", JsonUtils.objectToJson(carouselList));
        }else{
            carouselList=JsonUtils.jsonToList(carouselStr,Carousel.class);
        }
        return JSONResult.ok(carouselList);
    }

    /**
     *1.后台运营系统，一旦广告（轮播图）发生更改，就可以删除缓存进行重置
     *2.定时重置，比如每天凌晨重置
     *3.每个轮播图都有可能是一个广告，每个广告都会有一个过期时间，过期后重置
    * */

    /**
     * @Method cats
     * @Author zhengxin
     * @Description 获取商品分类(一级分类)
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2019/12/24 13:54
     */
    @ApiOperation(value = "获取商品分类(一级分类)",notes = "获取商品分类(一级分类)",httpMethod = "GET")
    @GetMapping("/cats")
    public JSONResult cats(){
        List<Category> categoryList=new ArrayList<>();
        String catStr=redisOperator.get("cats");
        if(StringUtils.isBlank(catStr)){//获取redis数据为空时
            //获取商品分类(一级分类)
            categoryList= categoryService.queryAllRootCategory();
            //将数据加入redis
            redisOperator.set("cats",JsonUtils.objectToJson(categoryList));
        }else{
            //redis存在该数据就从该数据中获取
            categoryList=JsonUtils.jsonToList(catStr,Category.class);
        }
        return JSONResult.ok(categoryList);
    }

    /**
     * @Method subCat
     * @Author zhengxin
     * @Description 通过一级分类id获取子分类
     * @param rootCatId 一级分类id
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2019/12/25 16:20
     */
    @ApiOperation(value = "获取商品子分类",notes = "获取商品子分类",httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public JSONResult subCat(
            @ApiParam(name = "rootCatId",value = "一级分类id",required = true)
            @PathVariable Integer rootCatId){
        if(rootCatId==null){
            return JSONResult.errorMsg("分类不存在");
        }

        List<CategoryVO> list=new ArrayList<>();
        String catStr=redisOperator.get("subCat:"+rootCatId);
        if(StringUtils.isBlank(catStr)){
            //通过一级分类id获取子分类
            list= categoryService.getSubCatList(rootCatId);
            redisOperator.set("subCat:"+rootCatId,JsonUtils.objectToJson(list));
        }else{
            list=JsonUtils.jsonToList(catStr,CategoryVO.class);
        }
        return JSONResult.ok(list);
    }

    /**
     * @Method sixNewItems
     * @Author zhengxin
     * @Description 查询每个一级分类下的最新6条商品数据
     * @param rootCatId 一级分类id
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2019/12/27 16:04
     */
    @ApiOperation(value = "查询每个一级分类下的最新6条商品数据",notes = "查询每个一级分类下的最新6条商品数据",httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public JSONResult sixNewItems(

            @ApiParam(name = "rootCatId",value = "一级分类id",required = true)
            @PathVariable Integer rootCatId){
        if(rootCatId==null){
            return JSONResult.errorMsg("分类不存在");
        }
        //通过一级分类id获取子分类
        List<NewItemsVO> list= categoryService.getSixNewItemsLazy(rootCatId);
        return JSONResult.ok(list);
    }
}
