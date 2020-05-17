package com.zx.controller;

import org.springframework.stereotype.Controller;

/**
 * @ClassName: BaseController
 * @Author: zhengxin
 * @Description: 基础类
 * @Date: 2020/5/16 23:02
 * @Version: 1.0
 */
@Controller
public class BaseController {
    //默认的评论每页数量
    public static final Integer COMMENT_PAGE_SIZE=10;

    //商品列表每页数量
    public static final Integer PAGE_SIZE=20;
}
