package com.zx.controller.center;

import com.zx.controller.BaseController;
import com.zx.enums.YesOrNoEnum;
import com.zx.pojo.OrderItems;
import com.zx.pojo.Orders;
import com.zx.pojo.bo.center.OrderItemsCommentBO;
import com.zx.service.center.MyCommentsService;
import com.zx.utils.JSONResult;
import com.zx.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: MyCommentsController
 * @Author: zhengxin
 * @Description: 用户中心评价模块相关接口
 * @Date: 2020/6/01 21:24
 * @Version: 1.0
 */
@Api(value = "用户中心评价模块", tags = {"用户中心评价模块相关接口"})
@RestController
@RequestMapping("mycomments")
public class MyCommentsController extends BaseController {

    @Autowired
    private MyCommentsService myCommentsService;

    /**
     * @Method pending
     * @Author zhengxin
     * @Description 查询订单列表
     * @param userId 用户id
     * @param orderId 订单id
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/6/1 21:25
     */
    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/pending")
    public JSONResult pending(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) {

        // 判断用户和订单是否关联
        JSONResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }
        // 判断该笔订单是否已经评价过，评价过了就不再继续
        Orders myOrder = (Orders)checkResult.getData();
        if (myOrder.getIsComment() == YesOrNoEnum.YES.type) {
            return JSONResult.errorMsg("该笔订单已经评价");
        }

        List<OrderItems> list = myCommentsService.queryPendingComment(orderId);

        return JSONResult.ok(list);
    }

    /**
     * @Method saveList
     * @Author zhengxin
     * @Description 保存评论列表
     * @param userId 用户id
     * @param orderId 订单id
     * @param commentList 商品评价集合
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/6/1 21:25
     */
    @ApiOperation(value = "保存评论列表", notes = "保存评论列表", httpMethod = "POST")
    @PostMapping("/saveList")
    public JSONResult saveList(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commentList) {

        System.out.println(commentList);

        // 判断用户和订单是否关联
        JSONResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }
        // 判断评论内容list不能为空
        if (commentList == null || commentList.isEmpty() || commentList.size() == 0) {
            return JSONResult.errorMsg("评论内容不能为空！");
        }

        myCommentsService.saveComments(orderId, userId, commentList);
        return JSONResult.ok();
    }

    /**
     * @Method query
     * @Author zhengxin
     * @Description 查询我的评价
     * @param userId 用户id
     * @param page 查询下一页的第几页
     * @param pageSize 分页的每一页显示的条数
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/6/1 21:27
     */
    @ApiOperation(value = "查询我的评价", notes = "查询我的评价", httpMethod = "POST")
    @PostMapping("/query")
    public JSONResult query(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = myCommentsService.queryMyComments(userId,
                page,
                pageSize);

        return JSONResult.ok(grid);
    }

}
