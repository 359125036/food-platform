package com.zx.service.impl;

import com.zx.enums.CategoryTypeEnum;
import com.zx.mapper.CategoryMapper;
import com.zx.mapper.CategoryMapperCustom;
import com.zx.pojo.Category;
import com.zx.pojo.vo.CategoryVO;
import com.zx.pojo.vo.NewItemsVO;
import com.zx.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CategoryServiceImpl
 * @Description: 商品分类
 * @Author: zhengxin
 * @Date: 2019/12/24 11:57
 * @Version: 1.0
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryMapperCustom categoryMapperCustom;
    /**
     * @Method queryAllRootCategory
     * @Author zhengxin
     * @Version  1.0
     * @Description 获取一级商品分类
     * @Return java.util.List<com.zx.pojo.Category>
     * @Exception
     * @Date 2019/12/24 12:00
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryAllRootCategory() {
        Example example=new Example(Category.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("type", CategoryTypeEnum.TOPCATEGORY.type);
        List<Category> categoryList=categoryMapper.selectByExample(example);
        return categoryList;
    }

    /**
     * @Method getSubCatList
     * @Author zhengxin
     * @Version  1.0
     * @Description 根据一级分类id查询子分类信息
     * @Return java.util.List<com.zx.pojo.vo.CategoryVO>
     * @Exception
     * @Date 2019/12/25 16:06
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) {
        return categoryMapperCustom.getSubCategoryList(rootCatId);
    }

    /**
     * @Method getSixNewItemsLazy
     * @Author zhengxin
     * @Version  1.0
     * @Description 查询首页每个一级分类下的6条最新商品数据
     * @Return java.util.List<com.zx.pojo.vo.NewItemsVO>
     * @Exception 
     * @Date 2019/12/27 15:59
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("rootCatId",rootCatId);
        return categoryMapperCustom.getSixNewItemsLazy(map);
    }
}
