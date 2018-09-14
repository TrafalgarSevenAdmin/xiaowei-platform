package com.xiaowei.wechat.handler;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.context.ContextUtils;
import com.xiaowei.wechat.builder.TextBuilder;
import com.xiaowei.wechat.entity.WxUser;
import com.xiaowei.wechat.service.IWxUserService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 订阅/关注公众号事件
 */
@Component
public class SubscribeHandler extends AbstractHandler {


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        IWxUserService wxUserService = ContextUtils.getApplicationContext().getBean(IWxUserService.class);
        this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        // 获取微信用户基本信息
        WxMpUser userWxInfo = weixinService.getUserService()
                .userInfo(wxMessage.getFromUser(), null);

        if (userWxInfo != null) {
            WxUser user = BeanCopyUtils.copy(userWxInfo, WxUser.class);
            //添加关注用户到本地
            user = wxUserService.saveOrUpdate(user);
            //如果存在真实名称就设置这个用户的备注
            if (user.getSysUser() != null) {
                if (StringUtils.isNotEmpty(user.getSysUser().getNickName())) {
                    weixinService.getUserService().userUpdateRemark(userWxInfo.getOpenId(), user.getSysUser().getNickName());
                }
                wxUserService.syncUserTag(user.getSysUser(), user.getOpenId());
            }
        }

        WxMpXmlOutMessage responseResult = null;
        try {
            responseResult = handleSpecial(wxMessage);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }

        try {
            return new TextBuilder().build("终于等到你，晓维快修竭诚为您服务！\n" +
                    "\n" +
                    "快修服务：晓维快修14年专注于提供商显设备、自助设备、安防等设备的专业维修服务。在【快速报修】一键免付报修，体验极速上门服务。\n" +
                    "在【我的工单】可查看工单服务进展和详情\n" +
                    "\n" +
                    "热线电话：400-1121-599", wxMessage, weixinService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以将其直接引导到保修工单页面
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage)
            throws Exception {
        //TODO
        return null;
    }

}
