package com.zx.service.impl;

import com.zx.mapper.CategoryMapper;
import com.zx.pojo.Category;
import com.zx.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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
        criteria.andEqualTo("type",1);
        List<Category> categoryList=categoryMapper.selectByExample(example);
        return categoryList;
    }
}
