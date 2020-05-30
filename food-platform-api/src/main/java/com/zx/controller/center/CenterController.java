package com.zx.controller.center;

import com.zx.pojo.Users;
import com.zx.service.center.CenterUserService;
import com.zx.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: CenterController
 * @Author: zhengxin
 * @Description: 用户中心
 * @Date: 2020/5/26 23:11
 * @Version: 1.0
 */
@Api(value = "center - 用户中心", tags = {"用户中心展示的相关接口"})
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    /**
     * @Method userInfo
     * @Author zhengxin
     * @Description 获取用户信息
     * @param userId 用户id
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/5/26 23:13
     */
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("userInfo")
    public JSONResult userInfo(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) {

        Users user = centerUserService.queryUserInfo(userId);
        return JSONResult.ok(user);
    }

}
