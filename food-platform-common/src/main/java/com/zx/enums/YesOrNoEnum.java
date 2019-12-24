package com.zx.enums;

/**
 * @ClassName: YesOrNoEnum
 * @Description: yes或no
 * @Author: zhengxin
 * @Date: 2019/12/24 11:13
 * @Version: 1.0
 */
public enum YesOrNoEnum {
    YES(1,"是"),
    NO(0,"否");

    public final Integer type;
    public final String value;

    YesOrNoEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
