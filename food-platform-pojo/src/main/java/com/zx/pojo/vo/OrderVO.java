package com.zx.pojo.vo;

import com.zx.pojo.bo.ShopcartBO;

import java.util.List;

public class OrderVO {

    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;

    private List<ShopcartBO> removeShopcartBOList;

    public List<ShopcartBO> getRemoveShopcartBOList() {
        return removeShopcartBOList;
    }

    public void setRemoveShopcartBOList(List<ShopcartBO> removeShopcartBOList) {
        this.removeShopcartBOList = removeShopcartBOList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrdersVO getMerchantOrdersVO() {
        return merchantOrdersVO;
    }

    public void setMerchantOrdersVO(MerchantOrdersVO merchantOrdersVO) {
        this.merchantOrdersVO = merchantOrdersVO;
    }
}