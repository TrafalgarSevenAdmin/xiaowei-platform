package com.xiaowei.wechat.service.impl;

import com.xiaowei.account.entity.SysRole;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.wechat.config.WechatProperties;
import com.xiaowei.wechat.entity.WxUser;
import com.xiaowei.wechat.repository.WxUserRepository;
import com.xiaowei.wechat.service.IWxUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WxUserServiceImpl extends BaseServiceImpl<WxUser> implements IWxUserService {

    @Autowired
    private WxUserRepository wxUserRepository;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WechatProperties wechatProperties;

    public WxUserServiceImpl(@Qualifier("wxUserRepository")BaseRepository repository) {
        super(repository);
    }

    @Override
    public void unsubscribe(String openId) {
        wxUserRepository.unsubscribe(openId,new Date());
    }

    @Override
    public Optional<WxUser> findByOpenId(String openId) {
        return wxUserRepository.findByOpenIdAndAppId(openId,wechatProperties.getAppId());
    }

    @Override
    public Optional<WxUser> findByMobile(String mobile) {
        return wxUserRepository.findBySysUser_MobileAndAppId(mobile,wechatProperties.getAppId());
    }


    @Override
    public Optional<WxUser> findByUserId(String userId) {
        return wxUserRepository.findBySysUser_IdAndAppId(userId,wechatProperties.getAppId());
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
        user.setLastUpdate(new Date());
        user.setAppId(wechatProperties.getAppId());
        return wxUserRepository.save(user);
    }

    /**
     * 同步用户标签
     * @param user  必须要含有roles
     * @param openId
     */
    @Override
    public void syncUserTag(SysUser user, String openId) throws WxErrorException {
        //获取这个用户的角色，并将角色名作为标签
        log.debug("正在同步用户:{}的角色:{}",user.getNickName(),user.getRoles().stream().map(SysRole::getName).collect(Collectors.toList()).toString());
        Map<String, SysRole> collect = user.getRoles().stream().collect(Collectors.toMap(v -> v.getName(), role -> role));
        //获得所有的标签
        List<WxUserTag> allTags = wxMpService.getUserTagService().tagGet();
        Set<String> allTagsName = allTags.stream().map(WxUserTag::getName).collect(Collectors.toSet());
        //找到没有创建的标签
        Collection<String> haventTag = CollectionUtils.subtract(collect.keySet(), allTagsName);
        for (String tag : haventTag) {
            //创建标签
            wxMpService.getUserTagService().tagCreate(tag);
        }
        Map<Long, WxUserTag> idMapTag = allTags.stream().collect(Collectors.toMap(v -> v.getId(), v -> v));
        Map<String, Long> tagMapId = allTags.stream().collect(Collectors.toMap(WxUserTag::getName,WxUserTag::getId));
        //获取这个用户含有的标签
        List<Long> tagIds = wxMpService.getUserTagService().userTagList(openId);
        Set<WxUserTag> haveTag = tagIds.stream().map(idMapTag::get).collect(Collectors.toSet());
        //增量更新用户标签
        Collection<Serializable> addTags = CollectionUtils.subtract(collect.keySet(), haveTag);
        Collection<Serializable> deleteTags = CollectionUtils.subtract(haveTag,collect.keySet());
        for (Serializable addTag : addTags) {
            try {
                wxMpService.getUserTagService().batchTagging(tagMapId.get(addTag), new String[]{openId});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Serializable deleteTag : deleteTags) {
            try {
                wxMpService.getUserTagService().batchUntagging(tagMapId.get(deleteTag), new String[]{openId});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置用户标签
     * @param openId
     * @param tag
     * @throws WxErrorException
     */
    @Override
    public void setUserTag(String openId, String tag, List<WxUserTag> wxUserTags) throws WxErrorException {
        Optional<WxUserTag> first = wxUserTags.stream().filter(wxTag -> wxTag.getName().equals(tag)).findFirst();
        WxUserTag wxUserTag = null;
        if (!first.isPresent()) {
            wxUserTag = wxMpService.getUserTagService().tagCreate(tag);
        } else {
            wxUserTag = first.get();
        }
        wxMpService.getUserTagService().batchTagging(wxUserTag.getId(), new String[]{openId});
    }

    /**
     * 设置用户标签
     * @param openId
     * @param tag
     * @throws WxErrorException
     */
    @Override
    public void setUserTag(String openId, String tag) throws WxErrorException {
        List<WxUserTag> wxUserTags = wxMpService.getUserTagService().tagGet();
        this.setUserTag(openId, tag, wxUserTags);
    }



}
