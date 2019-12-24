package com.zx.service.impl;

import com.zx.mapper.CarouselMapper;
import com.zx.pojo.Carousel;
import com.zx.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName: CarouselServiceImpl
 * @Description: 轮播业务层
 * @Author: zhengxin
 * @Date: 2019/12/24 10:58
 * @Version: 1.0
 */
@Service
public class CarouselServiceImpl implements CarouselService {
    @Autowired
    private CarouselMapper carouselMapper;
    /**
     * @Method queryAllCarousel
     * @Author zhengxin
     * @Version  1.0
     * @Description 获取轮播图列表
     * @Return java.util.List<com.zx.pojo.Carousel>
     * @Exception
     * @Date 2019/12/24 11:06
     */
    @Override
    public List<Carousel> queryAllCarousel(Integer isShow) {
        Example example=new Example(Carousel.class);
        //根据sort倒序排列
        example.orderBy("sort").desc();
        Example.Criteria criteria= example.createCriteria();
        //根据isShow查询
        criteria.andEqualTo("isShow",isShow);
        //获取轮播列表
        List<Carousel> carouselList=carouselMapper.selectByExample(example);
        return carouselList;
    }
}
