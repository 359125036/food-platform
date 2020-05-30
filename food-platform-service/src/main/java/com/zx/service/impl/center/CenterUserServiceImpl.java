package com.zx.service.impl.center;

import com.zx.mapper.UsersMapper;
import com.zx.pojo.Users;
import com.zx.pojo.bo.center.CenterUserBO;
import com.zx.service.center.CenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @ClassName: CenterUserServiceImpl
 * @Author: zhengxin
 * @Description: 用户中心 业务层
 * @Date: 2020/5/26 23:03
 * @Version: 1.0
 */
@Service
public class CenterUserServiceImpl implements CenterUserService {

    @Autowired
    private UsersMapper usersMapper;
    /**
     * @Method queryUserInfo
     * @Author zhengxin
     * @Description 查询用户信息
     * @param userId 用户id
     * @Return com.zx.pojo.Users
     * @Exception
     * @Date 2020/5/26 23:05
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Users users=usersMapper.selectByPrimaryKey(userId);
        users.setPassword(null);
        return users;
    }

    /**
     * @Method updateUserInfo
     * @Author zhengxin
     * @Description 修改用户信息
     * @param userId 用户id
     * @param centerUserBO
     * @Return com.zx.pojo.Users
     * @Exception
     * @Date 2020/5/26 23:06
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users updateUsers=new Users();
        //拷贝
        BeanUtils.copyProperties(centerUserBO,updateUsers);
        updateUsers.setId(userId);
        updateUsers.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(updateUsers);
        return queryUserInfo(userId);
    }

    /**
     * @Method updateUserFace
     * @Author zhengxin
     * @Description 用户头像更新
     * @param userId 用户id
     * @param faceUrl 图片地址
     * @Return com.zx.pojo.Users
     * @Exception
     * @Date 2020/5/26 23:10
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserFace(String userId, String faceUrl) {
        Users updateUser=new Users();
        updateUser.setId(userId);
        updateUser.setFace(faceUrl);
        updateUser.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(updateUser);
        return queryUserInfo(userId);
    }
}
