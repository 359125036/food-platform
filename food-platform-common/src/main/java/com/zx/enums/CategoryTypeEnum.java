package com.zx.enums;

/**
 * @Enum CategoryTypeEnum
 * @Author zhengxin
 * @Version 1.0
 * @Description 商品分类类型枚举
 * @Return
 * @Exception
 * @Date 2019/12/24 16:27
 */
public enum CategoryTypeEnum {
    TOPCATEGORY(1, "一级大分类"),
    SECONDCATEGORY(2, "二级分类"),
    THREECATEGORY(3, "三级小分类");

    public final Integer type;
    public final String value;

    CategoryTypeEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
