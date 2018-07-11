package com.xiaowei.wechat.handler;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.wechat.builder.TextBuilder;
import com.xiaowei.wechat.entity.WxUser;
import com.xiaowei.wechat.service.IWxUserService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 订阅/关注公众号事件
 */
@Component
public class SubscribeHandler extends AbstractHandler {

    @Autowired
    private IWxUserService wxUserService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        // 获取微信用户基本信息
        WxMpUser userWxInfo = weixinService.getUserService()
                .userInfo(wxMessage.getFromUser(), null);

        if (userWxInfo != null) {
            WxUser user = BeanCopyUtils.copy(userWxInfo, WxUser.class);
            //添加关注用户到本地
            wxUserService.saveOrUpdate(user);
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
            return new TextBuilder().build("感谢关注", wxMessage, weixinService);
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
