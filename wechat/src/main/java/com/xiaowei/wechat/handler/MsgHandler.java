package com.xiaowei.wechat.handler;

import com.xiaowei.wechat.builder.TextBuilder;
import com.xiaowei.wechat.utils.JsonUtils;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

@Component
public class MsgHandler extends AbstractHandler {

  @Autowired
  private WxMpService wxMpService;

  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                  Map<String, Object> context, WxMpService weixinService,
                                  WxSessionManager sessionManager) {

    if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
      //TODO 可以选择将消息保存到本地
    }


    //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
    try {
      if (StringUtils.startsWithAny(wxMessage.getContent(), "通知")) {
        WxMpTemplateMessage template = new WxMpTemplateMessage();
        template.setTemplateId("s_AZ6iMdu2cp7Lfj9JgHcH_1jox8nQvt-7-wh5hsKbI");
        List<WxMpTemplateData> data = new ArrayList<>();
        data.add(new WxMpTemplateData("Engineer", "张三", "#9932CC"));
        template.setData(data);
        template.setToUser(wxMessage.getFromUser());
//        template.setUrl("www.baidu.com");
        wxMpService.getTemplateMsgService().sendTemplateMsg(template);
        return null;
      }
      if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
          && weixinService.getKefuService().kfOnlineList()
          .getKfOnlineList().size() > 0) {
        return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
            .fromUser(wxMessage.getToUser())
            .toUser(wxMessage.getFromUser()).build();
      }
    } catch (WxErrorException e) {
      e.printStackTrace();
    }

    //TODO 组装回复消息
    String content = "收到信息内容：" + JsonUtils.toJson(wxMessage);

    return new TextBuilder().build(content, wxMessage, weixinService);

  }

}
