package com.xiaowei.wechat.controller;

import com.xiaowei.core.result.Result;
import com.xiaowei.wechat.dto.MeunDTO;
import com.xiaowei.wechat.dto.WechatMenuDto;
import com.xiaowei.wechat.service.IMeunService;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpGetSelfMenuInfoResult;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 微信菜单管理
 */
@RestController
@RequestMapping("/menu")
public class WxMenuController {

  @Autowired
  private IMeunService meunService;

  @Autowired
  private WxMpService wxService;

  /**
   * <pre>
   * 默认菜单创建接口
   * 由于查找不到自定义菜单时候所显示的默认菜单
   * </pre>
   */
  @PostMapping("/create")
  @RequiresPermissions("wechat:menu:create")
  public Result menuCreate(@RequestBody WxMenu menus) throws WxErrorException {
    wxService.getMenuService().menuCreate(menus);
    return Result.getSuccess();
  }

  @DeleteMapping("/{id}")
  @RequiresPermissions("wechat:menu:delete")
  public Result Delete(@PathVariable("id")String  id) throws WxErrorException {
    wxService.getMenuService().menuDelete(id);
    return Result.getSuccess();
  }

  /**
   * <pre>
   * 测试个性化菜单匹配结果
   * 详情请见: http://mp.weixin.qq.com/wiki/0/c48ccd12b69ae023159b4bfaa7c39c20.html
   * </pre>
   *
   * @param userid 可以是粉丝的OpenID，也可以是粉丝的微信号。
   *
   **/
  @GetMapping("/menuTryMatch/{userid}")
  @RequiresPermissions("wechat:menu:match")
  public WxMenu menuTryMatch(@PathVariable String userid) throws WxErrorException {
    return this.wxService.getMenuService().menuTryMatch(userid);
  }

  @GetMapping("/tag/{userid}")
  @RequiresPermissions("wechat:tag:user")
  public List<Long> tag(@PathVariable String userid) throws WxErrorException {
    return this.wxService.getUserTagService().userTagList(userid);
  }

  /**
   * 获取自定义菜单
   * @return
   * @throws WxErrorException
   */
  @GetMapping("/menuGet")
  @RequiresPermissions("wechat:menu:get")
  public WxMpMenu menuGet() throws WxErrorException {
    return this.wxService.getMenuService().menuGet();
  }

  /**
   * <pre>
   * 获取自定义菜单配置接口
   * 本接口将会提供公众号当前使用的自定义菜单的配置，如果公众号是通过API调用设置的菜单，则返回菜单的开发配置，而如果公众号是在公众平台官网通过网站功能发布菜单，则本接口返回运营者设置的菜单配置。
   * 请注意：
   * 1、第三方平台开发者可以通过本接口，在旗下公众号将业务授权给你后，立即通过本接口检测公众号的自定义菜单配置，并通过接口再次给公众号设置好自动回复规则，以提升公众号运营者的业务体验。
   *    * 2、本接口与自定义菜单查询接口的不同之处在于，本接口无论公众号的接口是如何设置的，都能查询到接口，而自定义菜单查询接口则仅能查询到使用API设置的菜单配置。
   *    * 3、认证/未认证的服务号/订阅号，以及接口测试号，均拥有该接口权限。
   *    * 4、从第三方平台的公众号登录授权机制上来说，该接口从属于消息与菜单权限集。
   *    * 5、本接口中返回的图片/语音/视频为临时素材（临时素材每次获取都不同，3天内有效，通过素材管理-获取临时素材接口来获取这些素材），本接口返回的图文消息为永久素材素材（通过素材管理-获取永久素材接口来获取这些素材）。
   *    *  接口调用请求说明:
   * http请求方式: GET（请使用https协议）
   * https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=ACCESS_TOKEN
   * </pre>
   */
  @GetMapping("/getSelfMenuInfo")
  @RequiresPermissions("wechat:menu:self:get")
  public WxMpGetSelfMenuInfoResult getSelfMenuInfo() throws WxErrorException {
    return this.wxService.getMenuService().getSelfMenuInfo();
  }

  /**
   * 设置微信的菜单
   * 同时也设置了菜单相关的角色
   * @return
   */
  @ApiOperation("设置微信的角色菜单")
  @PostMapping("/role/{roleId}")
  @RequiresPermissions("wechat:menu:set")
  public Result setWechatMenu(@PathVariable("roleId") String roleId, @RequestBody WechatMenuDto wechatMenuDto) throws WxErrorException {
      return meunService.setRoleWechtMenu(roleId, wechatMenuDto);
  }

  /**
   * 回现设置的微信菜单
   * @return
   */
  @ApiOperation("回现设置的微信菜单")
  @GetMapping("/role/{roleId}")
  @RequiresPermissions("wechat:menu:get")
  public Result getWechatMenu(@PathVariable("roleId") String roleId) throws WxErrorException {
      return Result.getSuccess(meunService.getRoleWechtMenu(roleId));
  }

}
