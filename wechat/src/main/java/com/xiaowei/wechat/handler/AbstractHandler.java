package com.xiaowei.wechat.handler;

import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  添加日志接口
 */
public abstract class AbstractHandler implements WxMpMessageHandler {
  protected Logger logger = LoggerFactory.getLogger(getClass());
}
