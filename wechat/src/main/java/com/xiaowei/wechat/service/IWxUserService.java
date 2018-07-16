package com.xiaowei.wechat.service;

import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.wechat.entity.WxUser;

import java.util.Optional;


public interface IWxUserService extends IBaseService<WxUser> {

    /**
     * 取消关注
     * @param openId
     */
    void unsubscribe(String openId);

    Optional<WxUser> findByOpenId(String openId);

    Optional<WxUser> findByMobile(String mobile);

    /**
     * 通过用户id查找绑定的微信的用户信息
     * @param userId
     * @return
     */
    Optional<WxUser> findByUserId(String userId);

    /**
     * 保存或更新微信发过来的用户信息
     * @param user
     */
    WxUser saveOrUpdate(WxUser user);
}
