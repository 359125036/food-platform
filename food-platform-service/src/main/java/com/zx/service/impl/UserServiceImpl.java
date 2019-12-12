package com.zx.service.impl;

import com.zx.mapper.StuMapper;
import com.zx.mapper.UsersMapper;
import com.zx.pojo.Stu;
import com.zx.pojo.Users;
import com.zx.service.StuService;
import com.zx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserNameIsExist(String userName) {
        Example userExample=new Example(Users.class);
        Example.Criteria userCriteria=userExample.createCriteria();
        //添加where比对条件
        userCriteria.andEqualTo("username",userName);
        Users result=usersMapper.selectOneByExample(userExample);
        return result==null ? false : true;
    }



}
