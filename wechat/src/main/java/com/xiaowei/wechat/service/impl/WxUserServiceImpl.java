package com.xiaowei.wechat.service.impl;

import com.xiaowei.account.entity.SysRole;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.wechat.config.WechatProperties;
import com.xiaowei.wechat.entity.WxUser;
import com.xiaowei.wechat.repository.WxUserRepository;
import com.xiaowei.wechat.service.IWxUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class WxUserServiceImpl extends BaseServiceImpl<WxUser> implements IWxUserService {

    @Autowired
    private WxUserRepository wxUserRepository;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WechatProperties wechatProperties;

    @Autowired
    private ISysUserService sysUserService;

    public WxUserServiceImpl(@Qualifier("wxUserRepository")BaseRepository repository) {
        super(repository);
    }

    @Override
    public void unsubscribe(String openId) {
        wxUserRepository.unsubscribe(openId,new Date());
        final Optional<WxUser> byOpenId = this.findByOpenId(openId);
        if (byOpenId.isPresent()) {
            final WxUser user = byOpenId.get();
            if (user.getSysUser() != null) {
                //更新用户信息为取消关注
                sysUserService.updateSubWechat(user.getSysUser().getId(), false);
            }
        }
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
    public List<WxUser> findByUserId(String userId) {
        return wxUserRepository.findBySysUser_IdAndAppId(userId,wechatProperties.getAppId());
    }

    @Override
    public WxUser saveOrUpdate(WxUser user) {
        //部分字段取自数据库,防止被更新
        Optional<WxUser> optionalWxUser = this.findByOpenId(user.getOpenId());
        if (optionalWxUser.isPresent()) {
            WxUser tempUser = optionalWxUser.get();
            //不允许代码中出现取消关联系统用户的情况存在
            if (user.getSysUser() == null) {
                user.setSysUser(tempUser.getSysUser());
            }
            user.setUnsubscribeTime(tempUser.getUnsubscribeTime());
        }
        user.setLastUpdate(new Date());
        user.setAppId(wechatProperties.getAppId());
        return wxUserRepository.save(user);
    }

    @Override
    @Transactional
    public void syncUser(String userId) throws WxErrorException {
        log.info("开始用户{}的数据",userId);
        SysUser sysUser = sysUserService.findById(userId);
        //查询到用户，且关注了微信后
        if (sysUser != null) {
            //查询用户的openid
           this.findByUserId(userId).stream().filter(WxUser::getSubscribe).forEach(v-> {
               try {
                   this.syncUser(sysUser, v.getOpenId());
               } catch (WxErrorException e) {
                   log.error("同步用户"+sysUser.getNickName()+"角色到微信失败！",e);
               }
           });
        }else {
            //若找不到此用户，可能这个用户已经被删除了。因此把此用户绑定的微信用户给解绑
            for (WxUser wxUser : this.findByUserId(userId)) {
                wxUser.setSysUser(null);
                wxUserRepository.save(wxUser);
            }
        }
    }

    /**
     * 同步用户标签
     * @param user  必须要含有roles
     * @param openId
     */
    @Override
    @Transactional
    public void syncUser(SysUser user, String openId) throws WxErrorException {
        if (CollectionUtils.isEmpty(user.getRoles())) {
            return;
        }
        //获取这个用户的角色，并将角色名作为标签
        log.debug("正在同步用户:{}的角色:{}",user.getNickName(), user.getRoles().stream().filter(v -> StringUtils.isNotEmpty(v.getWechatMenuId())).map(SysRole::getName).collect(Collectors.toList()).toString());
        //添加备注
        if (StringUtils.isNotEmpty(user.getNickName())) {
            wxMpService.getUserService().userUpdateRemark(openId, user.getNickName());
        }
        Map<String, SysRole> collect = user.getRoles().stream().filter(v -> StringUtils.isNotEmpty(v.getWechatMenuId())).collect(Collectors.toMap(v -> v.getName(), role -> role,(oldValue, newValue) -> newValue));
        //获得所有的标签
        List<WxUserTag> allTags = wxMpService.getUserTagService().tagGet();
        Set<String> allTagsName = allTags.stream().map(WxUserTag::getName).collect(Collectors.toSet());
        //找到没有创建的标签
        Collection<String> haventTag = CollectionUtils.subtract(collect.keySet(), allTagsName);
        for (String tag : haventTag) {
            //创建标签
            wxMpService.getUserTagService().tagCreate(tag);
        }
        Map<Long, WxUserTag> idMapTag = allTags.stream().collect(Collectors.toMap(v -> v.getId(), v -> v,(oldValue,newValue) -> newValue));
        Map<String, Long> tagMapId = allTags.stream().collect(Collectors.toMap(WxUserTag::getName,WxUserTag::getId,(oldValue,newValue) -> newValue));
        //获取这个用户含有的标签
        List<Long> tagIds = wxMpService.getUserTagService().userTagList(openId);
        Set<WxUserTag> haveTag = tagIds.stream().map(idMapTag::get).collect(Collectors.toSet());
        //增量更新用户标签
        final List<String> havaTagName = haveTag.stream().map(WxUserTag::getName).collect(Collectors.toList());
        Collection<Serializable> addTags = CollectionUtils.subtract(collect.keySet(), havaTagName);
        Collection<Serializable> deleteTags = CollectionUtils.subtract(havaTagName,collect.keySet());
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

    @Override
    public void bindUser(String userId, String openId) throws WxErrorException {
        WxUser wxUser = wxUserRepository.findByOpenIdAndAppId(openId, wechatProperties.getAppId()).orElseThrow(() -> new BusinessException("没有发现微信用户！"));
        SysUser sysUser = wxUser.getSysUser();
        if (sysUser != null) {
            log.warn("此微信用户{}已经绑定了一个系统用户：{},替换中。。。", wxUser.getNickname(), sysUser.getNickName());
        }
        sysUser = sysUserService.findById(userId);
        EmptyUtils.assertObject(sysUser, "要绑定的系统用户不存在，系统用户id：" + userId);
        wxUser.setSysUser(sysUser);
        wxUserRepository.save(wxUser);
        this.syncUser(sysUser,openId);

    }


}
