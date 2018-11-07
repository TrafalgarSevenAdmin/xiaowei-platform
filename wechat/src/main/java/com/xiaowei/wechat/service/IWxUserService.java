package com.xiaowei.wechat.service;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.wechat.entity.WxUser;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;

import java.util.List;
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
    List<WxUser> findByUserId(String userId);

    /**
     * 保存或更新微信发过来的用户信息
     * @param user
     */
    WxUser saveOrUpdate(WxUser user);

    void syncUser(String userId) throws WxErrorException;

    void sysUserDelete(String userId);

    void syncUser(SysUser user, String openId) throws WxErrorException;

    void setUserTag(String openId, String tag, List<WxUserTag> wxUserTags) throws WxErrorException;

    /**
     * 添加用户标签
     * @param openId
     * @param tag
     * @throws WxErrorException
     */
    void setUserTag(String openId, String tag) throws WxErrorException;

    /**
     * 绑定用户微信
     * @param userId
     * @param openId
     */
    void bindUser(String userId, String openId) throws WxErrorException;
}
