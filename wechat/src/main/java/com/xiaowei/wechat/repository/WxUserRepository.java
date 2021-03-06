package com.xiaowei.wechat.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.wechat.entity.WxUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface WxUserRepository extends BaseRepository<WxUser> {

    Optional<WxUser> findByOpenIdAndAppId(String openid,String appId);

    Optional<WxUser> findBySysUser_MobileAndAppId(String mobile,String appId);

    @Transactional
    @Modifying
    @Query("update WxUser set subscribe = false,unsubscribeTime=:unsubscribeTime where openId =:openid")
    void unsubscribe(@Param("openid") String openid, @Param("unsubscribeTime") Date unsubscribeTime);

    List<WxUser> findBySysUser_IdAndAppId(String userId, String appId);
}
