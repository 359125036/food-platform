package com.zx.controller;

import com.zx.pojo.Users;
import com.zx.pojo.bo.ShopcartBO;
import com.zx.pojo.bo.UserBO;
import com.zx.service.UserService;
import com.zx.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PassportController
 * @Description: 登录及退出
 * @Author: zhengxin
 * @Date: 2019/12/6 11:41
 * @Version: 1.0
 */
@Api(value = "注册登录",tags = {"用于注册登录的相关接口"})
@RestController//@RestController返回的都是json
@RequestMapping("passport")
public class PassportController extends BaseController{
    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;
    /**
     * @Method usernameIsExist
     * @Author zhengxin
     * @Description 验证用户名是否存在
     * @param username 用户名
     * @Return int
     * @Exception
     * @Date 2019/12/11 14:04
     */
    @ApiOperation(value = "用户名是否存在",notes = "用户名是否存在",httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public JSONResult usernameIsExist(@RequestParam String username){
        //1.判断用户名为空
        if(StringUtils.isBlank(username))
            return JSONResult.errorMsg("用户名不能为空");
        //2.查找用户名是否存在
        boolean isExist=userService.queryUserNameIsExist(username);

        if(isExist)
            return JSONResult.errorMsg("用户名已存在");
        //3.请求成功，用户名没有重复
        return JSONResult.ok();
    }

    /**
     * @Method regist
     * @Author zhengxin
     * @Description 用户注册
     * @param userBO 页面提交参数
     * @param request
     * @param response
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2019/12/13 14:14
     */
    @ApiOperation(value = "用户注册",notes = "用户注册",httpMethod = "POST")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody UserBO userBO, HttpServletRequest request,
                             HttpServletResponse response){
        String username=userBO.getUsername();
        String password=userBO.getPassword();
        String confirmPwd=userBO.getConfirmPassword();
       //1.用户名密码是否为空
        if(StringUtils.isBlank(username)||
                StringUtils.isBlank(password)||
                    StringUtils.isBlank(confirmPwd))
            return JSONResult.errorMsg("用户名和密码不能为空");
        //2.用户名是否存在
        boolean isExist=userService.queryUserNameIsExist(username);
        if(isExist)
            return JSONResult.errorMsg("用户名已存在");
        //3.密码长度是否大于6
        if(password.length()<6||confirmPwd.length()<6)
            return JSONResult.errorMsg("密码长度不小于6");
        //4.验证两次密码是否一致
        if(!password.equals(confirmPwd))
            return JSONResult.errorMsg("二次密码不一致");
        //5.创建用户
        Users user=userService.createUser(userBO);
        //6.将用户隐私信息设置为null
        user=setNullProperty(user);
        //7.将用户信息添加到cookie中
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(user),true);
        //TODO 生成用户token存入redis会话
        //同步购物车数据
        synchShopcartData(user.getId(), request,response );

        return JSONResult.ok();
    }

    /**
     * @Method synchShopcartData
     * @Author zhengxin
     * @Description 注册登录成功后，同步cookie和redis中的购物车数据
     * @param
     * @Return void
     * @Exception
     * @Date 2020/9/7 10:43
     */
    private void synchShopcartData(String userId, HttpServletRequest request,
                                   HttpServletResponse response){
        /**
         * 1.redis中无数据，如果cookie中的购物车为空，则不做任何操作
         *                 如果cookie中的购物车不为空，则直接放入redis
         * 2.redis中有数据，如果cookie中的购物车为空，则把redis的购物车覆盖本地cookie
         *                 如果cookie中的购物车不为空，且cookie中的某个商品在redis中存在，
         *                 则以cookie为主，删除redis中的数据，并把cookie中的商品直接覆盖redis
         * 3.同步到redis中，覆盖本地cookie购物车的数据
         */

        //从redis中获取购物车数据
        String shopcartJsonRedis=redisOperator.get(FOODIE_SHOPCART+":"+userId);

        //从cookie中获取购物车数据
        String shopcartCookie=CookieUtils.getCookieValue(request,FOODIE_SHOPCART,true);

        if(StringUtils.isBlank(shopcartJsonRedis)){
            //redis为空，cookie不为空，直接把cookie中数据放入redis
            if(StringUtils.isNotBlank(shopcartCookie)){
                redisOperator.set(FOODIE_SHOPCART+":"+userId,shopcartCookie);
            }
        }else{
            //redis不为空，cookie为空
            if(StringUtils.isBlank(shopcartCookie)){
                CookieUtils.setCookie(request,response,FOODIE_SHOPCART,shopcartJsonRedis,true);
            }else{
                /**
                 * 1.已经存在的，把cookie中对应的数量，覆盖redis
                 * 2.该项商品标记为待删除，统一放入一个待删除的list
                 * 3.从cookie中清理所有的待删除list
                 * 4.合并redis和cookie中的数据
                 * 5.更新到redis和cookie中
                 */

                List<ShopcartBO> shopcartListRedis=JsonUtils.jsonToList(shopcartJsonRedis,ShopcartBO.class );
                List<ShopcartBO> shopcartListCookie=JsonUtils.jsonToList(shopcartCookie,ShopcartBO.class );

                //待删除list
                List<ShopcartBO> pendingDeleteList=new ArrayList<>();

                for (ShopcartBO redisShopcart : shopcartListRedis) {
                    String redisSpecId=redisShopcart.getSpecId();

                    for (ShopcartBO cookieShopcart : shopcartListCookie) {
                        String cookiepecId=cookieShopcart.getSpecId();

                        if(redisSpecId.equals(cookiepecId)){
                            //覆盖购买数量
                            redisShopcart.setBuyCounts(cookieShopcart.getBuyCounts());
                            //把cookieShopcart放入待删除列表
                            pendingDeleteList.add(cookieShopcart);
                        }
                    }
                }

                //从现有cookie中删除对应的覆盖过的商品数据
                shopcartListCookie.removeAll(pendingDeleteList);
                //合并两个list
                shopcartListRedis.addAll(shopcartListCookie);

                //更新redis和cookie
                CookieUtils.setCookie(request,response ,FOODIE_SHOPCART , JsonUtils.objectToJson(shopcartListRedis),true);
                redisOperator.set(FOODIE_SHOPCART+":"+userId,JsonUtils.objectToJson(shopcartListRedis));

            }
        }
    }

    /**
     * @Method login
     * @Author zhengxin
     * @Description 用户登录
     * @param userBO 页面提交参数
     * @param request
     * @param response
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2019/12/19 10:50
     */
    @ApiOperation(value = "用户登录",notes = "用户登录",httpMethod = "POST")
    @PostMapping("/login")
    public JSONResult login(@RequestBody UserBO userBO, HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        String username=userBO.getUsername();
        String password=userBO.getPassword();
        //1.用户名密码是否为空
        if(StringUtils.isBlank(username)|| StringUtils.isBlank(password))
            return JSONResult.errorMsg("用户名和密码不能为空");
        // 2.实现登录
        Users user=userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if(user==null)
            return JSONResult.errorMsg("用户名或密码不正确");
        //3.将用户隐私信息设置为null
        user=setNullProperty(user);
        //4.将用户信息添加到cookie中
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(user),true);
        //TODO 生成用户token存入redis会话
        //同步购物车数据
        synchShopcartData(user.getId(), request,response );
        return JSONResult.ok(user);
    }

    /**
     * @Method setNullProperty
     * @Author zhengxin
     * @Description 将用户隐私信息设置为null
     * @Return com.zx.pojo.Users
     * @Exception
     * @Date 2019/12/19 16:46
     */
    private Users setNullProperty(Users user){
        user.setPassword(null);
        user.setMobile(null);
        user.setEmail(null);
        user.setCreatedTime(null);
        user.setUpdatedTime(null);
        user.setBirthday(null);
        return user;
    }
    /**
     * @Method logout
     * @Author zhengxin
     * @Description 用户退出登录
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2019/12/24 9:20
     */
    @ApiOperation(value = "用户退出登录",notes = "用户退出登录",httpMethod = "POST")
    @PostMapping("/logout")
    public JSONResult logout(@RequestParam String userId, HttpServletRequest request,
                             HttpServletResponse response){
        //清除用户相关的cookie信息
        CookieUtils.deleteCookie(request,response,"user");
        //用户退出登录，需要清空购物车
        CookieUtils.deleteCookie(request,response,FOODIE_SHOPCART);
        //TODO 分布式会话中需要清除用户数据
        return JSONResult.ok();
    }

}
