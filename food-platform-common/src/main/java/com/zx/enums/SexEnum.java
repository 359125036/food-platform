package com.zx.enums;
/**
 * @ClassName: SexEnum
 * @Author: zhengxin
 * @Description: 性别 枚举
 * @Date: 2020/5/25 22:05
 * @Version: 1.0
 */
public enum SexEnum {

    woman(0,"女"),
    man(1,"男"),
    secret(2,"保密");

    public final Integer type;
    public final String value;

    SexEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
