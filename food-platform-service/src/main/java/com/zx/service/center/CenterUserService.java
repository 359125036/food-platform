package com.zx.service.center;

import com.zx.pojo.Users;
import com.zx.pojo.bo.center.CenterUserBO;

/**
 * @ClassName: CenterUserService
 * @Author: zhengxin
 * @Description: 用户中心接口
 * @Date: 2020/5/26 23:02
 * @Version: 1.0
 */
public interface CenterUserService {

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    public Users queryUserInfo(String userId);

    /**
     * 修改用户信息
     * @param userId
     * @param centerUserBO
     */
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 用户头像更新
     * @param userId
     * @param faceUrl
     * @return
     */
    public Users updateUserFace(String userId, String faceUrl);
}
