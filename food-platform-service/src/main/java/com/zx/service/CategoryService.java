package com.zx.service;

import com.zx.pojo.Category;

import java.util.List;

//商品分类接口
public interface CategoryService {
    /**
     * @Method queryAllRootCategory
     * @Author zhengxin
     * @Version  1.0
     * @Description 获取所有的一级分类
     * @Return java.util.List<com.zx.pojo.Category>
     * @Exception
     * @Date 2019/12/24 11:56
     */
    public List<Category> queryAllRootCategory();
}
