package com.zx.service;

import com.zx.pojo.Category;
import com.zx.pojo.vo.CategoryVO;
import com.zx.pojo.vo.NewItemsVO;

import java.util.List;

/**
 * @ClassName: CategoryService
 * @Author: zhengxin
 * @Description: 商品分类接口
 * @Date: 2020/5/16 17:02
 * @Version: 1.0
 */
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

    /**
     * @Method getSixNewItemsLazy
     * @Author zhengxin
     * @Version  1.0
     * @Description 查询首页每个一级分类下的6条最新商品数据
     * @Return java.util.List<com.zx.pojo.vo.NewItemsVO>
     * @Exception 
     * @Date 2019/12/27 15:58
     */
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
