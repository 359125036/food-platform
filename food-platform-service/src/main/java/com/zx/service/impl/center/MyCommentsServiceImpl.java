package com.zx.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.zx.enums.YesOrNoEnum;
import com.zx.mapper.ItemsCommentsMapperCustom;
import com.zx.mapper.OrderItemsMapper;
import com.zx.mapper.OrderStatusMapper;
import com.zx.mapper.OrdersMapper;
import com.zx.pojo.OrderItems;
import com.zx.pojo.OrderStatus;
import com.zx.pojo.Orders;
import com.zx.pojo.bo.center.OrderItemsCommentBO;
import com.zx.pojo.vo.MyCommentVO;
import com.zx.service.center.MyCommentsService;
import com.zx.utils.PageUtil;
import com.zx.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MyCommentsServiceImpl
 * @Author: zhengxin
 * @Description: 个人评价 业务层
 * @Date: 2020/6/1 21:46
 * @Version: 1.0
 */
@Service
public class MyCommentsServiceImpl implements MyCommentsService {

    @Autowired
    public OrderItemsMapper orderItemsMapper;

    @Autowired
    public OrdersMapper ordersMapper;

    @Autowired
    public OrderStatusMapper orderStatusMapper;

    @Autowired
    public ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Autowired
    private Sid sid;

    /**
     * @Method queryPendingComment
     * @Author zhengxin
     * @Description 根据订单id查询关联的商品
      * @param orderId 订单id
     * @Return  java.util.List<com.zx.pojo.OrderItems>
     * @Exception
     * @Date 2020/6/1 21:32
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComment(String orderId) {
        OrderItems query = new OrderItems();
        query.setOrderId(orderId);
        return orderItemsMapper.select(query);
    }

    /**
     * @Method saveComments
     * @Author zhengxin
     * @Description 保存用户的评论
     * @param orderId 订单id
     * @param userId 用户id
     * @param commentList 商品订单评价集合
     * @Return void
     * @Exception
     * @Date 2020/6/1 21:32
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String orderId, String userId,
                             List<OrderItemsCommentBO> commentList) {

        // 1. 保存评价 items_comments
        for (OrderItemsCommentBO oic : commentList) {
            oic.setCommentId(sid.nextShort());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("commentList", commentList);
        itemsCommentsMapperCustom.saveComments(map);

        // 2. 修改订单表改已评价 orders
        Orders order = new Orders();
        order.setId(orderId);
        order.setIsComment(YesOrNoEnum.YES.type);
        ordersMapper.updateByPrimaryKeySelective(order);

        // 3. 修改订单状态表的留言时间 order_status
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    /**
     * @Method queryMyComments
     * @Author zhengxin
     * @Description  我的评价查询 分页
     * @param userId 用户id
     * @param page 页码
     * @param pageSize 每页数量
     * @Return com.zx.utils.PagedGridResult
     * @Exception
     * @Date 2020/6/1 21:34
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyComments(String userId,
                                           Integer page,
                                           Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> list = itemsCommentsMapperCustom.queryMyComments(map);

        return PageUtil.page(list, page);
    }
}
