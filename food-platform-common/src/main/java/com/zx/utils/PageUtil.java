package com.zx.utils;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @ClassName: PageUtil
 * @Author: zhengxin
 * @Description: 分页
 * @Date: 2020/5/16 22:53
 * @Version: 1.0
 */
public class PageUtil {

    public static PagedGridResult page(List<?> list,Integer page){
        PageInfo<?> pageList=new PageInfo<>(list);
        PagedGridResult grid=new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
