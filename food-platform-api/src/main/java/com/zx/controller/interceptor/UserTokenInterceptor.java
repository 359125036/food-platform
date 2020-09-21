package com.zx.controller.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: UserTokenInterceptor
 * @Author: zhengxin
 * @Description: 用户分布式会话拦截器
 * @Date: 2020/9/21 10:22
 * @Version: 1.0
 */
public class UserTokenInterceptor implements HandlerInterceptor {

    /**
     * @Method preHandle
     * @Author zhengxin
     * @Description 拦截请求，在访问controller之前
     * @param request
     * @param response
     * @param handler
     * @Return boolean
     * @Exception
     * @Date 2020/9/21 10:27
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("拦截成功。。。。");
        /**
         * false:请求被拦截，被驳回，验证出现问题
         * true：请求经过验证后，可放行
         */
        return false;
    }

    /**
     * @Method postHandle
     * @Author zhengxin
     * @Description 请求访问controller之后，渲染视图之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @Return void
     * @Exception
     * @Date 2020/9/21 10:25
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {


    }

    /**
     * @Method afterCompletion
     * @Author zhengxin
     * @Description 请求访问controller之后，渲染视图之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @Return void
     * @Exception
     * @Date 2020/9/21 10:26
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
