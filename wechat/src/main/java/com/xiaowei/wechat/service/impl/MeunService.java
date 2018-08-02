package com.xiaowei.wechat.service.impl;

import com.xiaowei.wechat.dto.MeunDTO;
import com.xiaowei.wechat.service.IMeunService;
import me.chanjar.weixin.common.bean.menu.WxMenuRule;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMenuService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeunService implements IMeunService {

    @Autowired
    private WxMpService wxMpService;

    /**
     * 可以通过标签名自定义标签
     * @param meunDTOs
     * @throws WxErrorException
     */
    @Override
    public void individuationMeun(MeunDTO meunDTOs) throws WxErrorException {
        WxMpMenuService menuService = wxMpService.getMenuService();
        menuService.menuDelete();
        List<WxUserTag> wxUserTags = wxMpService.getUserTagService().tagGet();
        for (MeunDTO.MeunDetail meunDTO : meunDTOs.getMeuns()) {
            if (StringUtils.isNotEmpty(meunDTO.getTagName())) {
                Long tagId;
                Optional<WxUserTag> first = wxUserTags.stream().filter(v -> v.getName().equals(meunDTO.getTagName())).findFirst();
                if (!first.isPresent()) {
                    //创建这个标签
                    WxUserTag wxUserTag = wxMpService.getUserTagService().tagCreate(meunDTO.getTagName());
                    tagId = wxUserTag.getId();
                } else {
                    tagId = first.get().getId();
                }
                if (meunDTO.getMenu().getMatchRule() == null) {
                    meunDTO.getMenu().setMatchRule(new WxMenuRule());
                }
                meunDTO.getMenu().getMatchRule().setTagId(String.valueOf(tagId));
            }
            menuService.menuCreate(meunDTO.getMenu());
        }
    }
}

