package com.zx.controller;

import com.zx.pojo.Items;
import com.zx.pojo.ItemsImg;
import com.zx.pojo.ItemsParam;
import com.zx.pojo.ItemsSpec;
import com.zx.pojo.vo.CommentLevelCountsVO;
import com.zx.pojo.vo.ItemInfoVO;
import com.zx.service.impl.ItemServiceImpl;
import com.zx.utils.JSONResult;
import com.zx.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: ItemController
 * @Author: zhengxin
 * @Description: ${description}
 * @Date: 2020/5/16 17:32
 * @Version: 1.0
 */
@Api(value = "商品接口",tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping("items")
public class ItemController extends BaseController{
    @Autowired
    private ItemServiceImpl itemService;

    @ApiOperation(value = "查询商品详情",notes = "查询商品详情",httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public JSONResult info(
            @ApiParam(name = "itemId",value = "商品id",required = true)
            @PathVariable String itemId){
        //判断商品id是否为空
        if(StringUtils.isBlank(itemId))
            return JSONResult.errorMsg(null);
        //获取商品详情
        Items items=itemService.queryItemById(itemId);
        //获取商品图片列表
        List<ItemsImg> itemsImgList= itemService.queryItemImgList(itemId);
        //获取商品规格列表
        List<ItemsSpec> itemsSpecList= itemService.queryItemSpecList(itemId);
        //获取商品参数
        ItemsParam itemsParam = itemService.queryItemsParam(itemId);
        ItemInfoVO itemInfoVO=new ItemInfoVO();
        itemInfoVO.setItem(items);
        itemInfoVO.setItemImgList(itemsImgList);
        itemInfoVO.setItemParams(itemsParam);
        itemInfoVO.setItemSpecList(itemsSpecList);
        return JSONResult.ok(itemInfoVO);
    }

    /**
     * 查询商品评价等级
     * @param itemId
     * @return
     */
    @ApiOperation(value = "查询商品评价等级",notes = "查询商品评价等级",httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JSONResult commentLevel(
            @ApiParam(name = "itemId",value = "商品id",required = true)
            @RequestParam String itemId){
        //判断商品id是否为空
        if(StringUtils.isBlank(itemId))
            return JSONResult.errorMsg(null);
        //获取商品评价等级数量
        CommentLevelCountsVO countsVO=itemService.queryCommentCounts(itemId);
        return JSONResult.ok(countsVO);
    }

    /**
     * 查询商品评论内容
     * @param itemId
     * @param level
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询商品评论内容",notes = "查询商品评论内容",httpMethod = "GET")
    @GetMapping("/comments")
    public JSONResult comments(
            @ApiParam(name = "itemId",value = "商品id",required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level",value = "评价等级",required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page",value = "查询的第几页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每页数量",required = false)
            @RequestParam Integer pageSize){
        //判断商品id是否为空
        if(StringUtils.isBlank(itemId))
            return JSONResult.errorMsg(null);
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = COMMENT_PAGE_SIZE;
        PagedGridResult grid=itemService.queryPagedComments(itemId,level,page,pageSize);
        return JSONResult.ok(grid);
    }


    /**
     * @Method: search
     * @Author: zhengxin
     * @Description: 搜索商品列表
     * @Date: 2020/5/17 11:01
     * @Exception:
     */

    @ApiOperation(value = "搜索商品列表",notes = "搜索商品列表",httpMethod = "GET")
    @GetMapping("/search")
    public JSONResult search(
            @ApiParam(name = "keywords",value = "关键字",required = false)
            @RequestParam String keywords,
            @ApiParam(name = "sort",value = "排序",required = false)
            @RequestParam String sort,
            @ApiParam(name = "page",value = "查询的第几页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每页数量",required = false)
            @RequestParam Integer pageSize){
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = PAGE_SIZE;
        PagedGridResult grid=itemService.searchItems(keywords,sort,page,pageSize);
        return JSONResult.ok(grid);
    }
}
