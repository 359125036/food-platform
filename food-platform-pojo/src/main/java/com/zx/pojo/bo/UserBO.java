package com.zx.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: UserBO
 * @Description: 页面传送会后端的数据
 * @Author: zhengxin
 * @Date: 2019/12/12 13:53
 * @Version: 1.0
 */
@ApiModel(value = "用户对象BO", description = "从客户端，由用户传入的数据封装在此entity中")
public class UserBO {
    //用户名
    @ApiModelProperty(value = "用户名", name = "username", example = "test", required = true)
    private String username;
    //密码
    @ApiModelProperty(value = "密码", name = "password", example = "123456", required = true)
    private String password;
    //确认密码
    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "123456", required = false)
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
