package com.zx.service;


import com.zx.pojo.Carousel;

import java.util.List;

/**
 * @ClassName: CarouselService
 * @Author: zhengxin
 * @Description: 查询显示的轮播图列表
 * @Date: 2020/5/16 17:02
 * @Version: 1.0
 */
public interface CarouselService {

    /**
     * @Method queryAllCarousel
     * @Author zhengxin
     * @Version  1.0
     * @Description 查询显示的轮播图列表
     * @Return java.util.List<com.zx.pojo.Carousel>
     * @Exception
     * @Date 2019/12/24 10:57
     */
    public List<Carousel> queryAllCarousel(Integer isShow);
}
