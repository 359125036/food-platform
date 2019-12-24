package com.zx.service.impl;

import com.zx.enums.SexEnum;
import com.zx.mapper.UsersMapper;
import com.zx.pojo.Users;
import com.zx.pojo.bo.UserBO;
import com.zx.service.UserService;
import com.zx.utils.DateUtil;
import com.zx.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @ClassName: StuServiceImpl
 * @Description: TODO
 * @Author: zhengxin
 * @Date: 2019/12/9 17:01
 * @Version: 1.0
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private Sid sid;
    //默认头像地址
    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    /**
     * @Method queryUserNameIsExist
     * @Author zhengxin
     * @Version  1.0
     * @Description 判断用户名是否存在
     * @Return boolean
     * @Exception
     * @Date 2019/12/11 11:31
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserNameIsExist(String username) {
        Example userExample=new Example(Users.class);
        Example.Criteria userCriteria=userExample.createCriteria();
        //添加where比对条件
        userCriteria.andEqualTo("username",username);
        Users result=usersMapper.selectOneByExample(userExample);
        return result==null ? false : true;
    }
    /**
     * @Method createUser
     * @Author zhengxin
     * @Version  1.0
     * @Description 创建用户
     * @Return com.zx.pojo.Users
     * @Exception 
     * @Date 2019/12/12 13:59
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {
        Users user=new Users();
        //设置全局唯一的id（防止分布式id重复）
        user.setId(sid.nextShort());
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setFace(USER_FACE);
        user.setSex(SexEnum.secret.type);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        usersMapper.insert(user);
        return user;
    }

    /**
     * @Method queryUserForLogin
     * @Author zhengxin
     * @Version  1.0
     * @Description 检索用户名和密码是否匹配，用于登录
     * @Return com.zx.pojo.Users
     * @Exception 
     * @Date 2019/12/19 10:17
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example example=new Example(Users.class);
        Example.Criteria userCriteria=example.createCriteria();
        userCriteria.andEqualTo("username",username);
        userCriteria.andEqualTo("password",password);
        Users user= usersMapper.selectOneByExample(example);
        return user;
    }
}
