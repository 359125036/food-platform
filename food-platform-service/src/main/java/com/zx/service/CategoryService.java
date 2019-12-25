package com.zx.service;

import com.zx.pojo.Category;
import com.zx.pojo.vo.CategoryVO;

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

    /**
     * @Method getSubCatList
     * @Author zhengxin
     * @Version  1.0
     * @Description 根据一级分类id查询子分类信息
     * @Return java.util.List<com.zx.pojo.vo.CategoryVO>
     * @Exception 
     * @Date 2019/12/25 16:04
     */
    public List<CategoryVO> getSubCatList(Integer rootCatId);
}
