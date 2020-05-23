package com.zx.enums;

/**
 * @ClassName: PaymentStatusEnum
 * @Author: zhengxin
 * @Description: 支付中心订单支付状态 枚举
 * @Date: 2020/5/23 22:05
 * @Version: 1.0
 */
public enum PaymentStatusEnum {

    WAIT_PAY(10, "待付款"),
    PAID(20, "已付款，待发货"),
    WAIT_RECEIVE(30, "已发货，待收货"),
    SUCCESS(40, "交易成功"),
    CLOSE(50, "交易关闭");

    public final Integer type;
    public final String value;

    PaymentStatusEnum(Integer type, String value){
        this.type = type;
        this.value = value;
    }
}
