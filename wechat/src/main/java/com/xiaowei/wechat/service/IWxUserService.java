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
}
