package com.xiaowei.wechat.service;

import com.xiaowei.core.result.Result;
import com.xiaowei.wechat.dto.MeunDTO;
import com.xiaowei.wechat.dto.WechatMenuDto;
import me.chanjar.weixin.common.error.WxErrorException;

public interface IMeunService {

    /**
     * 自定义菜单
     * @param meunDTOs
     * @throws WxErrorException
     */
    void individuationMeun(MeunDTO meunDTOs) throws WxErrorException;

    Result setRoleWechtMenu(String roleId, WechatMenuDto wechatMenuDto) throws WxErrorException;

    /**
     * 获取此角色的微信菜单
     * @param roleId
     * @return
     */
    WechatMenuDto getRoleWechtMenu(String roleId) throws WxErrorException;
}
