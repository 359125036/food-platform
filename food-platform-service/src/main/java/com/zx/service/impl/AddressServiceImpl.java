package com.zx.service.impl;

import com.zx.enums.YesOrNoEnum;
import com.zx.mapper.UserAddressMapper;
import com.zx.pojo.UserAddress;
import com.zx.pojo.bo.AddressBO;
import com.zx.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: AddressServiceImpl
 * @Author: zhengxin
 * @Description: 地址相关
 * @Date: 2020/5/18 21:51
 * @Version: 1.0
 */
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private Sid sid;

    /**
     * @Method queryUserAddress
     * @Author zhengxin
     * @Description 查询用户地址列表
     * @param userId 用户id
     * @Return java.util.List<com.zx.pojo.UserAddress>
     * @Exception
     * @Date 2020/5/18 22:09
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryUserAddress(String userId) {

        UserAddress userAddress=new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

    /**
     * @Method addNewUserAddress
     * @Author zhengxin
     * @Description 添加地址
     * @param addressBO 用户地址实体
     * @Return int
     * @Exception
     * @Date 2020/5/18 22:09
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {
        //1.判断当前用户是否存在地址，如果没有则新增为默认地址
        Integer isDefault=0;
        List<UserAddress> list=this.queryUserAddress(addressBO.getUserId());
        if(list==null||list.isEmpty()||list.size()==0)
            isDefault=1;
        //2.保持地址到数据库
        String addUserId=sid.nextShort();
        UserAddress userAddress=new UserAddress();
        //将一个类与另一个类的相同属性拷贝
        BeanUtils.copyProperties(addressBO,userAddress);
        userAddress.setId(addUserId);
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.insert(userAddress);
    }

    /**
     * @Method updateUserAddress
     * @Author zhengxin
     * @Description 修改用户地址
     * @param addressBO
     * @Return void
     * @Exception
     * @Date 2020/5/19 13:57
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) {

        String addressId=addressBO.getAddressId();
        UserAddress userAddress=new UserAddress();
        //将一个类与另一个类的相同属性拷贝
        BeanUtils.copyProperties(addressBO,userAddress);
        userAddress.setId(addressId);
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }

    /**
     * @Method deleteUserAddress
     * @Author zhengxin
     * @Description 用户删除地址
     * @param userId
     * @param addressId
     * @Return void
     * @Exception
     * @Date 2020/5/19 14:02
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) {

        Example example=new Example(UserAddress.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("id",addressId);
        userAddressMapper.deleteByExample(example);
    }

    /**
     * @Method setDefalut
     * @Author zhengxin
     * @Description 修改默认地址
     * @param userId
     * @param addressId
     * @Return void
     * @Exception
     * @Date 2020/5/19 14:09
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void setDefalut(String userId, String addressId) {

        // 1. 查找默认地址，设置为不默认
        UserAddress queryAddress = new UserAddress();
        queryAddress.setUserId(userId);
        queryAddress.setIsDefault(YesOrNoEnum.YES.type);
        List<UserAddress> list  = userAddressMapper.select(queryAddress);
        for (UserAddress ua : list) {
            ua.setIsDefault(YesOrNoEnum.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(ua);
        }

        // 2. 根据地址id修改为默认的地址
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setId(addressId);
        defaultAddress.setUserId(userId);
        defaultAddress.setIsDefault(YesOrNoEnum.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);

    }
    /**
     * @Method queryUserAddres
     * @Author zhengxin
     * @Description 查询用户地址
     * @param userId 用户id
     * @param addressId 地址表id
     * @Return com.zx.pojo.UserAddress
     * @Exception
     * @Date 2020/5/23 21:11
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress queryUserAddres(String userId, String addressId) {

        UserAddress singleAddress = new UserAddress();
        singleAddress.setId(addressId);
        singleAddress.setUserId(userId);

        return userAddressMapper.selectOne(singleAddress);
    }
}
