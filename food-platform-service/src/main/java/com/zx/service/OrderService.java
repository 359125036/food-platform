package com.zx.service;

import com.zx.pojo.OrderStatus;
import com.zx.pojo.bo.ShopcartBO;
import com.zx.pojo.bo.SubmitOrderBO;
import com.zx.pojo.vo.OrderVO;

import java.util.List;

/**
 * @ClassName: OrderService
 * @Author: zhengxin
 * @Description: 订单相关
 * @Date: 2020/5/16 17:02
 * @Version: 1.0
 */
public interface OrderService {

    /**
     * 用于创建订单相关信息
     * @param submitOrderBO
     */
    public OrderVO createOrder(List<ShopcartBO> shopcartBOList, SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    public OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭超时未支付订单
     */
    public void closeOrder();
}
