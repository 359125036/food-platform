package com.zx.controller;

import com.zx.pojo.Users;
import com.zx.pojo.bo.UserBO;
import com.zx.service.UserService;
import com.zx.utils.CookieUtils;
import com.zx.utils.JSONResult;
import com.zx.utils.JsonUtils;
import com.zx.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class PassportController {
    @Autowired
    private UserService userService;

    /**
     * @Method userNameIsExist
     * @Author zhengxin
     * @Version  1.0
     * @Description 验证用户名是否存在
     * @Return int
     * @Exception
     * @Date 2019/12/11 14:04
     */
    @ApiOperation(value = "用户名是否存在",notes = "用户名是否存在",httpMethod = "GET")
    @GetMapping("/userNameIsExist")
    public JSONResult userNameIsExist(@RequestParam String userName){
        //1.判断用户名为空
        if(StringUtils.isBlank(userName))
            return JSONResult.errorMsg("用户名不能为空");
        //2.查找用户名是否存在
        boolean isExist=userService.queryUserNameIsExist(userName);

        if(isExist)
            return JSONResult.errorMsg("用户名已存在");
        //3.请求成功，用户名没有重复
        return JSONResult.ok();
    }

    /**
     * @Method regist
     * @Author zhengxin
     * @Version  1.0
     * @Description 用户注册
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2019/12/13 14:14
     */
    @ApiOperation(value = "用户注册",notes = "用户注册",httpMethod = "POST")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody UserBO userBO, HttpServletRequest request,
                             HttpServletResponse response){
        String userName=userBO.getUsername();
        String password=userBO.getPassword();
        String confirmPwd=userBO.getConfirmPassword();
       //1.用户名密码是否为空
        if(StringUtils.isBlank(userName)||
                StringUtils.isBlank(password)||
                    StringUtils.isBlank(confirmPwd))
            return JSONResult.errorMsg("用户名和密码不能为空");
        //2.用户名是否存在
        boolean isExist=userService.queryUserNameIsExist(userName);
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
        return JSONResult.ok();
    }

    /**
     * @Method login
     * @Author zhengxin
     * @Version  1.0
     * @Description 用户登录
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2019/12/19 10:50
     */
    @ApiOperation(value = "用户登录",notes = "用户登录",httpMethod = "POST")
    @PostMapping("/login")
    public JSONResult login(@RequestBody UserBO userBO, HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        String userName=userBO.getUsername();
        String password=userBO.getPassword();
        //1.用户名密码是否为空
        if(StringUtils.isBlank(userName)|| StringUtils.isBlank(password))
            return JSONResult.errorMsg("用户名和密码不能为空");
        // 2.实现登录
        Users user=userService.queryUserForLogin(userName, MD5Utils.getMD5Str(password));
        if(user==null)
            return JSONResult.errorMsg("用户名或密码不正确");
        //3.将用户隐私信息设置为null
        user=setNullProperty(user);
        //4.将用户信息添加到cookie中
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(user),true);
        return JSONResult.ok(user);
    }

    /**
     * @Method setNullProperty
     * @Author zhengxin
     * @Version  1.0
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
     * @Version  1.0
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
        //TODO 用户退出登录，需要清空购物车
        //TODO 分布式会话中需要清除用户数据
        return JSONResult.ok();
    }

}
