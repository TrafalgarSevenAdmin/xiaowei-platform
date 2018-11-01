package com.xiaowei.wechat.service;

import com.xiaowei.core.result.Result;
import com.xiaowei.wechat.dto.MeunDTO;
import com.xiaowei.wechat.dto.WechatMenuDto;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;

public interface IMeunService {


    Result setRoleWechtMenu(String roleId, WechatMenuDto wechatMenuDto) throws WxErrorException;

    /**
     * 获取此角色的微信菜单
     * @param roleId
     * @return
     */
    WechatMenuDto getRoleWechtMenu(String roleId) throws WxErrorException;
}
