package com.xiaowei.wechat.handler;

import com.alibaba.fastjson.JSONObject;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.context.ContextUtils;
import com.xiaowei.wechat.builder.TextBuilder;
import com.xiaowei.wechat.consts.MagicValueStore;
import com.xiaowei.wechat.dto.InvitationInfoDto;
import com.xiaowei.wechat.entity.WxUser;
import com.xiaowei.wechat.service.IWxUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * 订阅/关注公众号事件
 */
@Component
public class SubscribeHandler extends AbstractHandler {

    /**
     * 由于在此组件初始化时，很多服务未能注册，因此只能在程序运行时动态注册
     */
    private IWxUserService wxUserService;
    private RedisTemplate redisTemplate;
    private ISysUserService sysUserService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        if (wxUserService == null) {
            wxUserService = ContextUtils.getApplicationContext().getBean(IWxUserService.class);
        }
        if (redisTemplate == null) {
            redisTemplate = ContextUtils.getApplicationContext().getBean(RedisTemplate.class);
        }
        if (sysUserService == null) {
            sysUserService = ContextUtils.getApplicationContext().getBean(ISysUserService.class);
        }

        this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        // 获取微信用户基本信息
        WxMpUser userWxInfo = weixinService.getUserService()
                .userInfo(wxMessage.getFromUser(), null);

        if (userWxInfo != null) {
            WxUser user = BeanCopyUtils.copy(userWxInfo, WxUser.class);
            //扫码过来的邀请链接
            String ticket = wxMessage.getTicket();
            if (StringUtils.isNotBlank(ticket)) {
                RedisTemplate redisTemplate = ContextUtils.getApplicationContext().getBean(RedisTemplate.class);
                Object obj = redisTemplate.opsForValue().get(ticket);
                if (obj != null) {
                    InvitationInfoDto invitationInfo = (InvitationInfoDto) obj;
                    this.logger.info("获取到邀请关注信息:" + invitationInfo);
                    //绑定到系统用户中
                    String userId = invitationInfo.getUser();
                    //若之前的用户已经绑定过。。这里需要业务判断，此处直接替换之前绑定的用户。毕竟之前的用户已经取消关注了。
                    SysUser sysUser = sysUserService.findById(userId);
                    sysUser.setSubWechat(true);
                    user.setSysUser(sysUser);
                    //存储邀请信息
                    user.setInvitationInfo(JSONObject.toJSONString(invitationInfo));
                    sysUserService.saveUser(sysUser);
                    //绑定后,也许应该给业务系统推送消息说此用户绑定，需要推送相应的消息
                }
            }
            //添加关注用户到本地
            user = wxUserService.saveOrUpdate(user);
            if (user.getSysUser() != null) {
                //同步用户信息
                wxUserService.syncUserTag(user.getSysUser(), user.getOpenId());
            }
        }

        try {
            return new TextBuilder().build(MagicValueStore.WechatSubscribeMessage, wxMessage, weixinService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
        return null;
    }
}
