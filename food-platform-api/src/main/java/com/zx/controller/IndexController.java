package com.zx.controller;

import com.zx.enums.YesOrNoEnum;
import com.zx.pojo.Carousel;
import com.zx.pojo.Category;
import com.zx.pojo.vo.CategoryVO;
import com.zx.service.CarouselService;
import com.zx.service.CategoryService;
import com.zx.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * @Method carousel
     * @Author zhengxin
     * @Version  1.0
     * @Description 获取首页轮播图列表
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2019/12/24 11:22
     */
    @ApiOperation(value = "获取首页轮播图列表",notes = "获取首页轮播图列表",httpMethod = "GET")
    @GetMapping("/carousel")
    public JSONResult carousel(){
        //获取首页轮播图列表
        List<Carousel> carouselList= carouselService.queryAllCarousel(YesOrNoEnum.YES.type);
        return JSONResult.ok(carouselList);
    }

    /**
     * @Method cats
     * @Author zhengxin
     * @Version  1.0
     * @Description 获取商品分类(一级分类)
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2019/12/24 13:54
     */
    @ApiOperation(value = "获取商品分类(一级分类)",notes = "获取商品分类(一级分类)",httpMethod = "GET")
    @GetMapping("/cats")
    public JSONResult cats(){
        //获取商品分类(一级分类)
        List<Category> categoryList= categoryService.queryAllRootCategory();
        return JSONResult.ok(categoryList);
    }

    /**
     * @Method subCat
     * @Author zhengxin
     * @Version  1.0
     * @Description 通过一级分类id获取子分类
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2019/12/25 16:20
     */
    @ApiOperation(value = "获取商品子分类",notes = "获取商品子分类)",httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public JSONResult subCat(
            @ApiParam(name = "rootCatId",value = "一级分类id",required = true)
            @PathVariable Integer rootCatId){
        if(rootCatId==null){
            return JSONResult.errorMsg("分类不存在");
        }
        //通过一级分类id获取子分类
        List<CategoryVO> list= categoryService.getSubCatList(rootCatId);
        return JSONResult.ok(list);
    }
}
