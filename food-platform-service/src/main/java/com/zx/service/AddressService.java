package com.zx.service;

import com.zx.pojo.UserAddress;
import com.zx.pojo.bo.AddressBO;

import java.util.List;

/**
 * @ClassName: AddressService
 * @Author: zhengxin
 * @Description: 地址service
 * @Date: 2020/5/18 21:49
 * @Version: 1.0
 */
public interface AddressService {

    public List<UserAddress> queryUserAddress(String userId);

    public void addNewUserAddress(AddressBO addressBO);

    public void updateUserAddress(AddressBO addressBO);

    public void deleteUserAddress(String userId,String addressId);

    public void setDefalut(String userId,String addressId);
}
