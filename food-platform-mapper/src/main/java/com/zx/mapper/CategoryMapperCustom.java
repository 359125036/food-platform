package com.zx.mapper;

import com.zx.my.mapper.MyMapper;
import com.zx.pojo.Category;
import com.zx.pojo.vo.CategoryVO;

import java.util.List;

/**
 * @Interface CategoryMapperCustom
 * @Author zhengxin
 * @Version  1.0
 * @Description 自定义分类接口
 * @Return
 * @Exception
 * @Date 2019/12/25 15:43
 */
public interface CategoryMapperCustom  {

    public List<CategoryVO> getSubCategoryList(Integer rootCategoryId);
}