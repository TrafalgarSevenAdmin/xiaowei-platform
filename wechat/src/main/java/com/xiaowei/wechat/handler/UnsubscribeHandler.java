package com.xiaowei.wechat.handler;

import com.xiaowei.wechat.service.IWxUserService;
import com.xiaowei.wechat.service.impl.WxUserServiceImpl;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 取消关注公众号
 */
@Component
public class UnsubscribeHandler extends AbstractHandler {

  @Autowired
  private IWxUserService wxUserService;

  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                  Map<String, Object> context, WxMpService wxMpService,
                                  WxSessionManager sessionManager) {
    String openId = wxMessage.getFromUser();
    this.logger.info("取消关注用户 OPENID: " + openId);
    wxUserService.unsubscribe(openId);
    return null;
  }

}
