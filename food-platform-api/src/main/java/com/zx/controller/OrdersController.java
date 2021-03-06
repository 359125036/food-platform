package com.zx.controller;

import com.zx.enums.OrderStatusEnum;
import com.zx.enums.PayMethodEnum;
import com.zx.pojo.OrderStatus;
import com.zx.pojo.bo.ShopcartBO;
import com.zx.pojo.bo.SubmitOrderBO;
import com.zx.pojo.vo.MerchantOrdersVO;
import com.zx.pojo.vo.OrderVO;
import com.zx.service.OrderService;
import com.zx.utils.CookieUtils;
import com.zx.utils.JSONResult;
import com.zx.utils.JsonUtils;
import com.zx.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName: OrdersController
 * @Author: zhengxin
 * @Description: 订单相关
 * @Date: 2020/5/26 17:32
 * @Version: 1.0
 */
@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RequestMapping("orders")
@RestController
public class OrdersController extends BaseController {

    final static Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisOperator redisOperator;

    /**
     * @Method create
     * @Author zhengxin
     * @Description 用户下单
     * @param submitOrderBO
     * @param request
     * @param response
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/5/23 21:10
     */
    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (submitOrderBO.getPayMethod() != PayMethodEnum.WEIXIN.type
            && submitOrderBO.getPayMethod() != PayMethodEnum.ALIPAY.type ) {
            return JSONResult.errorMsg("支付方式不支持！");
        }
        String shopcart=redisOperator.get(FOODIE_SHOPCART+":"+submitOrderBO.getUserId());
        if(StringUtils.isBlank(shopcart)){
            return JSONResult.errorMsg("购物参数不正确");
        }

        List<ShopcartBO> shopcartBOList=JsonUtils.jsonToList(shopcart, ShopcartBO.class);
//        System.out.println(submitOrderBO.toString());

        // 1. 创建订单
        OrderVO orderVO = orderService.createOrder(shopcartBOList,submitOrderBO);
        String orderId = orderVO.getOrderId();

        //清理覆盖现有的redis汇总的购物数据
        shopcartBOList.removeAll(orderVO.getRemoveShopcartBOList());
        redisOperator.set(FOODIE_SHOPCART+":"+submitOrderBO.getUserId(),JsonUtils.objectToJson(shopcartBOList));
        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        /**
         * 1001
         * 2002 -> 用户购买
         * 3003 -> 用户购买
         * 4004
         */
        // 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartBOList), true);

        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        // 为了方便测试购买，所以所有的支付金额都统一改为1分钱
        merchantOrdersVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId","imooc");
        headers.add("password","imooc");

        HttpEntity<MerchantOrdersVO> entity =
                new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<JSONResult> responseEntity =
                restTemplate.postForEntity(paymentUrl,
                                            entity,
                        JSONResult.class);
        JSONResult paymentResult = responseEntity.getBody();
//        if (paymentResult.getStatus() != 200) {
//            logger.error("发送错误：{}", paymentResult.getMsg());
//            return JSONResult.errorMsg("支付中心订单创建失败，请联系管理员！");
//        }

        return JSONResult.ok(orderId);
    }

    /**
     * @Method notifyMerchantOrderPaid
     * @Author zhengxin
     * @Description 支付中心支付成功后调用该接口修改订单状态
     * @param merchantOrderId 订单id
     * @Return java.lang.Integer
     * @Exception
     * @Date 2020/5/23 19:01
     */
    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    /**
     * @Method getPaidOrderInfo
     * @Author zhengxin
     * @Description 获取订单支付信息
     * @param orderId 订单id
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/5/23 19:00
     */
    @PostMapping("getPaidOrderInfo")
    public JSONResult getPaidOrderInfo(String orderId) {
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return JSONResult.ok(orderStatus);
    }
}
