package com.xiaowei.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.xiaowei.account.entity.SysPermission;
import com.xiaowei.account.entity.SysRole;
import com.xiaowei.account.service.ISysPermissionService;
import com.xiaowei.account.service.ISysRoleService;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.utils.FastJsonUtils;
import com.xiaowei.wechat.dto.MeunDTO;
import com.xiaowei.wechat.dto.WechatMenuDto;
import com.xiaowei.wechat.entity.WxMenuEntity;
import com.xiaowei.wechat.repository.WxMenuEntityRepository;
import com.xiaowei.wechat.service.IMeunService;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.bean.menu.WxMenuRule;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMenuService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeunService implements IMeunService {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysPermissionService sysPermissionService;

    @Autowired
    private WxMenuEntityRepository wxMenuEntityRepository;


    @Override
    public Result setRoleWechtMenu(@PathVariable("roleId") String roleId, @RequestBody WechatMenuDto wechatMenuDto) throws WxErrorException {
        //角色的权限配置
        SysRole role = sysRoleService.findById(roleId);
        EmptyUtils.assertObject(role,"未找到对应的角色信息");
        //查询出这个角色所分配的所有的微信菜单权限
        List<SysPermission> havePermissions = role.getPermissions().stream().filter(v->v.getSymbol().indexOf("menu:wechat")>=0).collect(Collectors.toList());
        List<String> setIds = new ArrayList<>();
        //查找所有设置了的菜单权限id
        for (WechatMenuDto.OneLevelMenu menu : wechatMenuDto.getMenus()) {
            if (StringUtils.isNotEmpty(menu.getId())) {
                setIds.add(menu.getId());
            } else {
                if (CollectionUtils.isNotEmpty(menu.getSubButtons()) && menu.getSubButtons().size() <= 5) {
                    setIds.addAll(menu.getSubButtons());
                } else {
                    throw new BusinessException("微信子菜单必须含有且不能超过5项！");
                }
            }
        }
        if(CollectionUtils.isNotEmpty(setIds)){
            //配置新的权限
            role.getPermissions().removeAll(havePermissions);
            List<SysPermission> permissions = sysPermissionService.findByIds(setIds.toArray(new String[]{}));
            role.getPermissions().addAll(permissions);

            //微信的菜单配置
            WxMenu wxMenu = new WxMenu();
            Map<String, SysPermission> idMap = permissions.stream().collect(Collectors.toMap(SysPermission::getId, v -> v));

            List<WxMenuButton> buttons = new ArrayList<>();
            for (WechatMenuDto.OneLevelMenu menu : wechatMenuDto.getMenus()) {
                //如果只有一层
                if (StringUtils.isNotEmpty(menu.getId())) {
                    WxMenuButton button  = new WxMenuButton();
                    button.setType("view");
                    button.setUrl(idMap.get(menu.getId()).getUri());
                    button.setName(idMap.get(menu.getId()).getName());
                    buttons.add(button);
                } else {
                    //如果有多层
                    WxMenuButton button  = new WxMenuButton();
                    button.setName(menu.getName());
                    button.setSubButtons(menu.getSubButtons().stream().map(id->{
                        WxMenuButton wxMenuButton = new WxMenuButton();
                        wxMenuButton.setType("view");
                        wxMenuButton.setName(idMap.get(id).getName());
                        wxMenuButton.setUrl(idMap.get(id).getUri());
                        return wxMenuButton;
                    }).collect(Collectors.toList()));
                    buttons.add(button);
                }
            }
            wxMenu.setButtons(buttons);
            //配置微信的菜单过滤条件
            //判断分组是否存在，不存在就创建
            Optional<WxUserTag> first = wxMpService.getUserTagService().tagGet().stream().filter(v -> v.getName().equals(role.getName())).findFirst();
            Long tagId;
            if (!first.isPresent()) {
                //创建这个标签
                WxUserTag wxUserTag = wxMpService.getUserTagService().tagCreate(role.getName());
                tagId = wxUserTag.getId();
            } else {
                tagId = first.get().getId();
            }
            WxMenuRule matchRule = new WxMenuRule();
            matchRule.setTagId(String.valueOf(tagId));
            wxMenu.setMatchRule(matchRule);
            //若之前存在自定义菜单，就先删除
            if (StringUtils.isNotEmpty(role.getWechatMenuId())) {
                wxMpService.getMenuService().menuDelete(role.getWechatMenuId());
            }
            String wxMenuId = wxMpService.getMenuService().menuCreate(wxMenu);
            role.setWechatMenuId(wxMenuId);
            //将配置信息保存到数据库，以供回显
            WxMenuEntity wxMenuEntity = new WxMenuEntity(wxMenuId, FastJsonUtils.objectToJson(wechatMenuDto));
            wxMenuEntityRepository.save(wxMenuEntity);
        }else {
            //如果删除了所有的微信菜单，那就删除个性菜单字段以及删除微信的个性化菜单
            if (StringUtils.isNotEmpty(role.getWechatMenuId())) {
                wxMpService.getMenuService().menuDelete(role.getWechatMenuId());
                //删除数据库中保存的菜单数据
                wxMenuEntityRepository.deleteById(role.getWechatMenuId());
                role.setWechatMenuId(null);
            }
        }
        //保存到数据库中
        sysRoleService.save(role);
        return Result.getSuccess();
    }

    @Override
    public WechatMenuDto getRoleWechtMenu(String roleId) throws WxErrorException {
        //角色的权限配置
        SysRole role = sysRoleService.getOne(roleId);
        EmptyUtils.assertObject(role,"未找到对应的角色信息");
        if (StringUtils.isEmpty(role.getWechatMenuId())) {
            return new WechatMenuDto();
        }
        Optional<WxMenuEntity> byId = wxMenuEntityRepository.findById(role.getWechatMenuId());
        if (!byId.isPresent()) {
            return new WechatMenuDto();
        } else {
            return JSON.parseObject(byId.get().getData(), WechatMenuDto.class);
        }
    }
}

