package com.zx.controller.interceptor;

import com.zx.utils.JSONResult;
import com.zx.utils.JsonUtils;
import com.zx.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @ClassName: UserTokenInterceptor
 * @Author: zhengxin
 * @Description: 用户分布式会话拦截器
 * @Date: 2020/9/21 10:22
 * @Version: 1.0
 */
public class UserTokenInterceptor implements HandlerInterceptor {

    public static final String REDIS_USER_TOKEN = "redis_user_token";

    @Autowired
    private RedisOperator redisOperator;

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

        String userId=request.getHeader("headerUserId");
        String userToken=request.getHeader("headerUserToken");

        if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(userToken)){
            String uniqueToken=redisOperator.get(REDIS_USER_TOKEN+":"+userId);
            if(StringUtils.isNotBlank(uniqueToken)){
                if(!uniqueToken.equals(userToken)){
                    returnErrorResponse(response,JSONResult.errorMsg("账户在异地登录。。。"));
                    return false;
                }
            }else {
                returnErrorResponse(response,JSONResult.errorMsg("请登录。。。"));
                return false;
            }
        }else {
            returnErrorResponse(response,JSONResult.errorMsg("请登录。。。"));
            return false;
        }
        /**
         * false:请求被拦截，被驳回，验证出现问题
         * true：请求经过验证后，可放行
         */
        return true;
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

    /**
     * @Method returnErrorResponse
     * @Author zhengxin
     * @Description 输出拦截结果到页面
     * @param response
     * @param jsonResult
     * @Return void
     * @Exception
     * @Date 2020/9/21 14:35
     */
    public void returnErrorResponse(HttpServletResponse response,
                                    JSONResult jsonResult){

        OutputStream out=null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out=response.getOutputStream();
            out.write(JsonUtils.objectToJson(jsonResult).getBytes("utf-8"));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
