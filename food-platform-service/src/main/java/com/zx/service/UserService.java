package com.zx.service;


import com.zx.pojo.Users;
import com.zx.pojo.bo.UserBO;

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
    public boolean queryUserNameIsExist(String userName);

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

}
