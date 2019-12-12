package com.zx.controller;

import com.zx.pojo.Stu;
import com.zx.service.StuService;
import com.zx.service.UserService;
import com.zx.utils.JSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: PassportController
 * @Description: 登录验证
 * @Author: zhengxin
 * @Date: 2019/12/6 11:41
 * @Version: 1.0
 */
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



}
