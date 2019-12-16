package com.zx.controller;

import com.zx.pojo.bo.UserBO;
import com.zx.service.UserService;
import com.zx.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: PassportController
 * @Description: 登录验证
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
     * @Method register
     * @Author zhengxin
     * @Version  1.0
     * @Description 用户注册
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2019/12/13 14:14
     */
    @ApiOperation(value = "用户注册",notes = "用户注册",httpMethod = "POST")
    @PostMapping("/register")
    public JSONResult register(@RequestBody UserBO userBO){
        String userName=userBO.getUserName();
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
        userService.createUser(userBO);
        return JSONResult.ok();
    }


}
