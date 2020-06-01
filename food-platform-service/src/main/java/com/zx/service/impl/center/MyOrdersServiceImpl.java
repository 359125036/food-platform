package com.zx.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.zx.enums.OrderStatusEnum;
import com.zx.enums.YesOrNoEnum;
import com.zx.mapper.OrderStatusMapper;
import com.zx.mapper.OrdersMapper;
import com.zx.mapper.OrdersMapperCustom;
import com.zx.pojo.OrderStatus;
import com.zx.pojo.Orders;
import com.zx.pojo.vo.MyOrdersVO;
import com.zx.pojo.vo.OrderStatusCountsVO;
import com.zx.service.center.MyOrdersService;
import com.zx.utils.PageUtil;
import com.zx.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MyOrdersServiceImpl
 * @Author: zhengxin
 * @Description: 个人订单 业务层
 * @Date: 2020/6/1 21:47
 * @Version: 1.0
 */
@Service
public class MyOrdersServiceImpl implements MyOrdersService {

    @Autowired
    public OrdersMapperCustom ordersMapperCustom;

    @Autowired
    public OrdersMapper ordersMapper;

    @Autowired
    public OrderStatusMapper orderStatusMapper;

    /**
     * @Method queryMyOrders
     * @Author zhengxin
     * @Description 查询我的订单列表
     * @param userId 用户id
     * @param orderStatus 订单状态
     * @param page 页码
     * @param pageSize 每页数量
     * @Return com.zx.utils.PagedGridResult
     * @Exception
     * @Date 2020/6/1 21:34
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrders(String userId,
                                         Integer orderStatus,
                                         Integer page,
                                         Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        if (orderStatus != null) {
            map.put("orderStatus", orderStatus);
        }

        PageHelper.startPage(page, pageSize);

        List<MyOrdersVO> list = ordersMapperCustom.queryMyOrders(map);

        return PageUtil.page(list, page);
    }

    /**
     * @Method updateDeliverOrderStatus
     * @Author zhengxin
     * @Description  订单状态 --> 商家发货
     * @param orderId 订单id
     * @Return void
     * @Exception
     * @Date 2020/6/1 21:35
     */
    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) {

        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        updateOrder.setDeliverTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);

        orderStatusMapper.updateByExampleSelective(updateOrder, example);
    }

    /**
     * @Method queryMyOrder
     * @Author zhengxin
     * @Description 查询我的订单
     * @param userId 用户id
     * @param orderId 订单id
     * @Return com.zx.pojo.Orders
     * @Exception
     * @Date 2020/6/1 21:36
     */
    @Transactional(propagation=Propagation.SUPPORTS)
    @Override
    public Orders queryMyOrder(String userId, String orderId) {

        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setId(orderId);
        orders.setIsDelete(YesOrNoEnum.NO.type);

        return ordersMapper.selectOne(orders);
    }

    /**
     * @Method updateReceiveOrderStatus
     * @Author zhengxin
     * @Description 更新订单状态 —> 确认收货
     * @param orderId 订单id
     * @Return boolean
     * @Exception
     * @Date 2020/6/1 21:37
     */
    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public boolean updateReceiveOrderStatus(String orderId) {

        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        updateOrder.setSuccessTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);

        int result = orderStatusMapper.updateByExampleSelective(updateOrder, example);

        return result == 1 ? true : false;
    }

    /**
     * @Method deleteOrder
     * @Author zhengxin
     * @Description 删除订单
     * @param userId 用户id
     * @param orderId 订单id
     * @Return boolean
     * @Exception
     * @Date 2020/6/1 21:44
     */
    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public boolean deleteOrder(String userId, String orderId) {

        Orders updateOrder = new Orders();
        updateOrder.setIsDelete(YesOrNoEnum.YES.type);
        updateOrder.setUpdatedTime(new Date());

        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", orderId);
        criteria.andEqualTo("userId", userId);

        int result = ordersMapper.updateByExampleSelective(updateOrder, example);

        return result == 1 ? true : false;
    }

    /**
     * @Method getOrderStatusCounts
     * @Author zhengxin
     * @Description 查询用户订单数
     * @param userId 用户id
     * @Return com.zx.pojo.vo.OrderStatusCountsVO
     * @Exception
     * @Date 2020/6/1 21:45
     */
    @Transactional(propagation=Propagation.SUPPORTS)
    @Override
    public OrderStatusCountsVO getOrderStatusCounts(String userId) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        map.put("orderStatus", OrderStatusEnum.WAIT_PAY.type);
        int waitPayCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        int waitDeliverCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        int waitReceiveCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        map.put("isComment", YesOrNoEnum.NO.type);
        int waitCommentCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        OrderStatusCountsVO countsVO = new OrderStatusCountsVO(waitPayCounts,
                                                            waitDeliverCounts,
                                                            waitReceiveCounts,
                                                            waitCommentCounts);
        return countsVO;
    }

    /**
     * @Method getOrdersTrend
     * @Author zhengxin
     * @Description 获得分页的订单动向
     * @param userId 用户id
     * @param page 页码
     * @param pageSize 每页数量
     * @Return com.zx.utils.PagedGridResult
     * @Exception
     * @Date 2020/6/1 21:45
     */
    @Transactional(propagation=Propagation.SUPPORTS)
    @Override
    public PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);
        List<OrderStatus> list = ordersMapperCustom.getMyOrderTrend(map);

        return PageUtil.page(list, page);
    }
}
