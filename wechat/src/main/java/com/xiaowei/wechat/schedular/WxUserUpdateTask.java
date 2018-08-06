package com.xiaowei.wechat.schedular;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.wechat.entity.WxUser;
import com.xiaowei.wechat.service.IWxUserService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 微信用户信息同步。
 * 推荐每天晚上11点做。
 */
@Component
public class WxUserUpdateTask {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private IWxUserService wxUserService;

    /**
     * 每晚11点同步用户数据
     */
    @Scheduled(cron = "0 0 23 * * ?")
    public void rsync() {
        String nextOpenId = null;
        WxUser user;
        WxMpUser wxMpUser;
        try {
            do {
                WxMpUserList wxMpUserList = wxMpService.getUserService().userList(nextOpenId);
                nextOpenId = wxMpUserList.getNextOpenid();
                for (String openId : wxMpUserList.getOpenids()) {
                    wxMpUser = wxMpService.getUserService().userInfo(openId);
                    user = BeanCopyUtils.copy(wxMpUser, WxUser.class);
                    wxUserService.saveOrUpdate(user);
                }
            } while (StringUtils.isNotEmpty(nextOpenId));
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
}
