package com.zx.controller;

import com.zx.pojo.UserAddress;
import com.zx.pojo.bo.AddressBO;
import com.zx.service.AddressService;
import com.zx.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.utils.MobileEmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: AddressController
 * @Author: zhengxin
 * @Description: 地址相关接口
 * @Date: 2020/5/18 21:40
 * @Version: 1.0
 */
@Api(value = "地址相关",tags = {"地址相关的接口"})
@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * @Method list
     * @Author zhengxin
     * @Description 获取地址列表
     * @param userId 用户id
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/5/18 22:01
     */
    @ApiOperation(value = "获取地址列表",notes = "获取地址列表",httpMethod ="POST")
    @PostMapping("/list")
    public JSONResult list(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId){
        if(StringUtils.isBlank(userId))
            return  JSONResult.errorMsg(null);
        List<UserAddress> userAddressList= addressService.queryUserAddress(userId);
        return JSONResult.ok(userAddressList);
    }

    /**
     * @Method add
     * @Author zhengxin
     * @Description  用户新增地址
     * @param addressBO
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/5/18 22:38
     */
    @ApiOperation(value = "用户新增地址",notes = "用户新增地址",httpMethod ="POST")
    @PostMapping("/add")
    public JSONResult add(
            @ApiParam(name = "addressBO",value = "地址信息BO",required = true)
            @RequestBody AddressBO addressBO){
        //检查用户添加的地址信息是否有误
        JSONResult checkResult= checkAddress(addressBO);
        if(checkResult.getStatus()!=200)
            return checkResult;
        addressService.addNewUserAddress(addressBO);
        return JSONResult.ok();
    }

    /**
     * @Method update
     * @Author zhengxin
     * @Description 用户修改地址
     * @param addressBO
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/5/19 13:58
     */
    @ApiOperation(value = "用户修改地址",notes = "用户修改地址",httpMethod ="POST")
    @PostMapping("/update")
    public JSONResult update(
            @ApiParam(name = "addressBO",value = "地址信息BO",required = true)
            @RequestBody AddressBO addressBO){

        if(StringUtils.isBlank(addressBO.getAddressId()))
            return JSONResult.errorMsg("修改地址错误：addressId不能为空");
        //检查用户添加的地址信息是否有误
        JSONResult checkResult= checkAddress(addressBO);
        if(checkResult.getStatus()!=200)
            return checkResult;
        addressService.updateUserAddress(addressBO);
        return JSONResult.ok();
    }

    /**
     * @Method delete
     * @Author zhengxin
     * @Description 用户删除地址
     * @param userId 用户id
     * @param addressId 地址id
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/5/19 14:08
     */
    @ApiOperation(value = "用户删除地址",notes = "用户删除地址",httpMethod ="POST")
    @PostMapping("/delete")
    public JSONResult delete(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId",value = "地址id",required = true)
            @RequestParam String addressId){

        if(StringUtils.isBlank(userId)||StringUtils.isBlank(addressId))
            return JSONResult.errorMsg("");
        addressService.deleteUserAddress(userId,addressId);
        return JSONResult.ok();
    }

    /**
     * @Method setDefalut
     * @Author zhengxin
     * @Description 用户修改默认地址
     * @param userId 用户id
     * @param addressId 地址id
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/5/19 14:24
     */
    @ApiOperation(value = "用户修改默认地址",notes = "用户修改默认地址",httpMethod ="POST")
    @PostMapping("/setDefalut")
    public JSONResult setDefalut(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId",value = "地址id",required = true)
            @RequestParam String addressId){

        if(StringUtils.isBlank(userId)||StringUtils.isBlank(addressId))
            return JSONResult.errorMsg(null);
        addressService.setDefalut(userId,addressId);
        return JSONResult.ok();
    }

    /**
     * @Method checkAddress
     * @Author zhengxin
     * @Description  检查用户添加的地址信息是否有误
     * @param addressBO
     * @Return
     * @Exception
     * @Date 2020/5/18 22:38
     */
    private JSONResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return JSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return JSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return JSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return JSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return JSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return JSONResult.errorMsg("收货地址信息不能为空");
        }

        return JSONResult.ok();
    }
}
