package com.xiaowei.wechat.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.wechat.entity.WxUser;
import com.xiaowei.wechat.repository.WxUserRepository;
import com.xiaowei.wechat.service.IWxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


@Service
public class WxUserServiceImpl extends BaseServiceImpl<WxUser> implements IWxUserService {

    @Autowired
    private WxUserRepository wxUserRepository;

    public WxUserServiceImpl(@Qualifier("wxUserRepository")BaseRepository repository) {
        super(repository);
    }

    @Override
    public void unsubscribe(String openId) {
        wxUserRepository.unsubscribe(openId,new Date());
    }

    @Override
    public Optional<WxUser> findByOpenId(String openId) {
        return wxUserRepository.findByOpenId(openId);
    }

    @Override
    public Optional<WxUser> findByMobile(String mobile) {
        return wxUserRepository.findBySysUser_Mobile(mobile);
    }


    @Override
    public Optional<WxUser> findByUserId(String userId) {
        return wxUserRepository.findBySysUser_Id(userId);
    }

    @Override
    public WxUser saveOrUpdate(WxUser user) {
        //部分字段取自数据库,防止被更新
        Optional<WxUser> optionalWxUser = this.findByOpenId(user.getOpenId());
        if (optionalWxUser.isPresent()) {
            WxUser tempUser = optionalWxUser.get();
            user.setSysUser(tempUser.getSysUser());
            user.setUnsubscribeTime(tempUser.getUnsubscribeTime());
        }
        return wxUserRepository.save(user);
    }
}
