package com.zx.service;


import com.zx.pojo.Users;
import com.zx.pojo.bo.UserBO;

/**
 * @ClassName: UserService
 * @Author: zhengxin
 * @Description: 用户相关
 * @Date: 2020/5/16 17:02
 * @Version: 1.0
 */
public interface UserService {
    /**
     * @Method queryUserNameIsExist
     * @Author zhengxin
     * @Version  1.0
     * @Description 判断用户名是否存在
     * @Return boolean
     * @Exception
     * @Date 2019/12/11 11:31
     */
    public boolean queryUserNameIsExist(String username);

    /**
     * @Method createUser
     * @Author zhengxin
     * @Version  1.0
     * @Description 创建用户
     * @Return com.zx.pojo.Users
     * @Exception
     * @Date 2019/12/12 13:58
     */
    public Users createUser(UserBO userBO);

    /**
     * @Method queryUserForLogin
     * @Author zhengxin
     * @Version  1.0
     * @Description 检索用户名和密码是否匹配，用于登录
     * @Return com.zx.pojo.Users
     * @Exception
     * @Date 2019/12/19 10:16
     */
    public Users queryUserForLogin(String username,String password);

}
