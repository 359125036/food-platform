package com.zx.controller;

import com.zx.pojo.Orders;
import com.zx.pojo.Users;
import com.zx.pojo.vo.UserVO;
import com.zx.service.center.MyOrdersService;
import com.zx.utils.JSONResult;
import com.zx.utils.RedisOperator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.UUID;

/**
 * @ClassName: BaseController
 * @Author: zhengxin
 * @Description: 基础类
 * @Date: 2020/5/16 23:02
 * @Version: 1.0
 */
@Controller
public class BaseController {

    @Autowired
    private RedisOperator redisOperator;

    //用户redis使用的token
    public static final String REDIS_USER_TOKEN = "redis_user_token";


    public static final String FOODIE_SHOPCART = "shopcart";

    //默认的评论每页数量
    public static final Integer COMMENT_PAGE_SIZE=10;

    //商品列表每页数量
    public static final Integer PAGE_SIZE=20;

    //订单列表每页数量
    public static final Integer COMMON_PAGE_SIZE = 10;

    // 支付中心的调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";		// produce

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    //                       |-> 回调通知的url
    String payReturnUrl = "http://api.food.zhengxinwang.xyz/food-platform-api/orders/notifyMerchantOrderPaid";


    @Autowired
    public MyOrdersService myOrdersService;

    /**
     * @Method checkUserOrder
     * @Author zhengxin
     * @Description 用于验证用户和订单是否有关联关系，避免非法用户调用
     * @param userId 用户id
     * @param orderId 订单id
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/5/31 16:53
     */
    public JSONResult checkUserOrder(String userId, String orderId) {

        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return JSONResult.errorMsg("订单不存在！");
        }
        return JSONResult.ok(order);
    }


    /**
     * @Method conventUserVO
     * @Author zhengxin
     * @Description 实现用户的redis回话
     * @param users
     * @Return com.zx.pojo.vo.UserVO
     * @Exception
     * @Date 2020/9/21 9:55
     */
    public UserVO conventUserVO(Users users){

        //实现用户的redis回话
        String uniqueToken= UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN+":"+users.getId(), uniqueToken);
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(users, userVO);
        userVO.setUserUniqueToken(uniqueToken);
        return userVO;
    }

}
