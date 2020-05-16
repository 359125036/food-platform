package com.zx.enums;

/**
 * @ClassName: CommentLevel
 * @Author: zhengxin
 * @Description: 商品评价等级 枚举
 * @Date: 2020/5/16 21:05
 * @Version: 1.0
 */
public enum CommentLevel {
    GOOD(1,"好评"),
    NORMAL(2,"中评"),
    BAD(3,"差评");

    public final Integer type;

    public final String value;

    CommentLevel(Integer type,String value){
        this.type=type;
        this.value=value;
    }

}
