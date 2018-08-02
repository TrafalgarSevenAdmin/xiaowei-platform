package com.xiaowei.wechat.service;

import com.xiaowei.wechat.dto.MeunDTO;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

public interface IMeunService {

    /**
     * 自定义菜单
     * @param meunDTOs
     * @throws WxErrorException
     */
    void individuationMeun(MeunDTO meunDTOs) throws WxErrorException;
}
