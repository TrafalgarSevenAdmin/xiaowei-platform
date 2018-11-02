package com.xiaowei.wechat.controller;

import com.xiaowei.account.authorization.WxUserLoginToken;
import com.xiaowei.account.bean.LoginSysUserDTO;
import com.xiaowei.account.consts.AccountConst;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.service.ISysRoleService;
import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.account.utils.AccountUtils;
import com.xiaowei.accountcommon.LoginUserBean;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.context.ContextUtils;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.utils.RequestUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.wechat.consts.MagicValueStore;
import com.xiaowei.wechat.consts.ServerInfoProperties;
import com.xiaowei.wechat.dto.BindMobileDTO;
import com.xiaowei.wechat.entity.WxUser;
import com.xiaowei.wechat.service.IWxUserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Log
@RestController
@RequestMapping("/auth")
public class WechatAuthController {

    @Autowired
    private WxMpService wxMpService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ServerInfoProperties serverInfoProperties;

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 获取jsapi所需要的签名
     * @param url
     * @return
     * @throws WxErrorException
     */
    @GetMapping("jsapiSignature")
    public Result getJsapiSignature(String url) throws WxErrorException {
        return Result.getSuccess(wxMpService.createJsapiSignature(url));
    }

    @ApiOperation(value = "绑定手机号",notes = "绑定成功后，前端应该再次调用登陆接口，并可以指定回调地址。")
    @AutoErrorHandler
    @PostMapping("/bind")
    public Result bind(@RequestBody @Validated BindMobileDTO bindMobileDTO, HttpServletRequest request) throws WxErrorException {
        //绑定手机号
        String openId = (String)request.getSession().getAttribute("openId");
        if (StringUtils.isEmpty(openId)) {
            throw new BusinessException("来源错误！");
        }
        Optional<WxUser> wxUserByMobile = wxUserService.findByMobile(bindMobileDTO.getMobile());
        EmptyUtils.assertOptionalNot(wxUserByMobile,"此手机已绑定微信号!");

        //获取这个微信用户的信息
        Optional<WxUser> wxUser = wxUserService.findByOpenId(openId);
        //如果没有查出来该用户的信息，就说明此请求不是从登陆页面来的。。。
        EmptyUtils.assertOptional(wxUser,"来源错误！未获取到用户信息！");
        Optional<SysUser> sysUseByMobile = sysUserService.findByMobile(bindMobileDTO.getMobile());
        WxUser user = wxUser.get();
        SysUser sysUser;
        if (sysUseByMobile.isPresent()) {
            sysUser = sysUseByMobile.get();
            //如果有用户，就绑定到一起
            user.setSysUser(sysUser);
            //绑定到一起
            wxUserService.save(user);
            sysUserService.updateSubWechat(sysUser.getId(),true);
            //将系统中的角色绑定到微信中的标签中
            wxUserService.syncUser(sysUseByMobile.get(), user.getOpenId());
        } else {
//            sysUserService.saveUser()
//            如果没有就新建一个系统用户，标识为普通用户
            sysUser = new SysUser();
            sysUser.setLoginName(bindMobileDTO.getMobile());
            sysUser.setMobile(bindMobileDTO.getMobile());
            sysUser.setNickName(bindMobileDTO.getName());
            sysUser.setSubWechat(true);
            sysUser = sysUserService.registerUser(sysUser);
            user.setSysUser(sysUser);
            wxUserService.save(user);
            //同步用户标签
            wxUserService.syncUser(sysUser,user.getOpenId());
        }
        //登陆
        Subject subject = SecurityUtils.getSubject();
        subject.login(new WxUserLoginToken(sysUser.getLoginName(),ContextUtils.getIpAddr()));
        AccountUtils.loadUser();
        //绑定成功后，交给前端做路由
        return Result.getSuccess(request.getSession().getAttribute("redirect"));
    }

    @GetMapping("/back/info")
    public String authBackWithInfo(String code, String state, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //在此不对调用方做限制，毕竟会通过code去查询用户的openid，相当于code就做了校验，但是在支付回调接口中必须对请求过来的服务器地址做校验，判断是否是微信服务器发起的请求
        log.info("获取到微信回调的code:" + code);
        if (StringUtils.isEmpty(code)) {
            return "error";
        }
        try {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            Optional<WxUser> wxUserOptional = wxUserService.findByOpenId(wxMpOAuth2AccessToken.getOpenId());
            WxUser user;
            //如果在数据库中找不到了这个用户的信息
            if (!wxUserOptional.isPresent()) {
                //去尝试请求这个用户的详细信息
                WxMpUser wxMpUser;
                try {
                    //在关注了公众号后才会允许获得用户详情
                    wxMpUser = wxMpService.getUserService().userInfo(wxMpOAuth2AccessToken.getOpenId());
                    //如果获取到该用户
                    if (wxMpUser != null) {
                        //保存用户数据到数据库中
                        user = BeanCopyUtils.copy(wxMpUser, WxUser.class);
                        //原来的绑定信息不清除
                        user = wxUserService.saveOrUpdate(user);
                    } else {
                        throw new BusinessException("获取不到用户的信息，请重新授权");
                    }
                } catch (Exception e) {
                    // 判断是否拉取了很多次，如果超过了2次那么说明此用户没有授权或者其他错误什么的。
                    if (StringUtils.isEmpty(state)) {
                        state = UUID.randomUUID().toString();
                    }
                    //如果次数存在就判断是否超过了两次
                    int number = 1;
                    if (stringRedisTemplate.hasKey(MagicValueStore.wxStatesNumberPro + state)) {
                        String numbers = stringRedisTemplate.opsForValue().get(MagicValueStore.wxStatesNumberPro + state);
                        number = NumberUtils.toInt(numbers,0);
                        if (number > 2) {
                            //在这里报错
                            log.warning("用户信息获取错误！用户信息拉取已超过两次！");
                            throw new BusinessException("用户信息获取错误！请检查是否已经授权");
                        }
                        number++;
                    }
                    //追加操作
                    String key = MagicValueStore.wxStatesNumberPro + state;
                    stringRedisTemplate.opsForValue().set(key, "" + number);
                    //此信息1分钟后过期
                    stringRedisTemplate.expire(key, 1, TimeUnit.MINUTES);
                    log.warning("没有获取到该openId:"+wxMpOAuth2AccessToken.getOpenId()+"的用户信息，正在重新拉取。错误信息："+e.getMessage());
                    //打印一下错误堆栈，方便查看
                    e.printStackTrace();
                    //再次拉取用户详细信息
                    String url = wxMpService.oauth2buildAuthorizationUrl(serverInfoProperties.getHost() + "/wechat/auth/back/info", "snsapi_userinfo", state);
                    response.sendRedirect(url);
                    return null;
                }
            } else {
                user = wxUserOptional.get();
            }
            if (user.getSysUser() == null) {
                //微信中的这个用户是否绑定了我们的系统用户
                request.getSession().setAttribute("openId", wxMpOAuth2AccessToken.getOpenId());
                //如果没有绑定，就以访客身份登陆
                String lastCallBack = getLastCallBack(state);
                request.getSession().setAttribute("redirect", lastCallBack);
                Subject subject = SecurityUtils.getSubject();
                subject.login(new WxUserLoginToken(AccountConst.GUEST_USER_NAME,ContextUtils.getIpAddr()));
                subject.getSession().setAttribute(LoginUserUtils.SESSION_USER_KEY,AccountConst.GUEST_USER_INFO);
                subject.getSession().setAttribute(LoginUserUtils.LOGIN_USER_BROWSER, RequestUtils.getOsAndBrowserInfo());
                //若当前用户是访客，就把他的微信的openId存储到登陆信息中，方便其他系统获取
                subject.getSession().setAttribute("openId", wxMpOAuth2AccessToken.getOpenId());
                //在以访客身份登陆后,重新访问路由即可
                response.sendRedirect(lastCallBack);
            } else {
                //在此做登陆，就是向前端写统一的登陆cookies
                String loginName = wxUserOptional.get().getSysUser().getLoginName();
                Subject subject = SecurityUtils.getSubject();
                subject.login(new WxUserLoginToken(loginName,ContextUtils.getIpAddr()));
                AccountUtils.loadUser();
                String url  = getLastCallBack(state);
                response.sendRedirect(url);
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过state获取回调地址
     * @param state
     * @return
     */
    private String getLastCallBack(String state) {
        String url;//然后重定向到之前的地址中
        if (StringUtils.isEmpty(state) || !stringRedisTemplate.hasKey(MagicValueStore.wxStatesValuePro + state)) {
            //如果之前的回调信息是空的,或者在redis中的信息已经超时，直接跳转到默认的首页
            url = serverInfoProperties.getPreIndex();
        } else {
            //重定向到回调地址
            String callbackUrl = stringRedisTemplate.opsForValue().get(MagicValueStore.wxStatesValuePro + state);
            //如果回调是一个网站
            if (callbackUrl.indexOf(".") > 0) {
                url = callbackUrl;
            } else {
                //否者拼装成一个网址
                url = serverInfoProperties.getHost() + callbackUrl;
            }
        }
        //获取过后就删除缓存
        stringRedisTemplate.delete(MagicValueStore.wxStatesValuePro + state);
        stringRedisTemplate.delete(MagicValueStore.wxStatesNumberPro + state);
        return url;
    }

    /**
     * 调用此接口登陆
     *
     * @param callback
     * @param response
     * @throws IOException
     */
    @GetMapping(path = "/login")
    public void login(String callback,HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(serverInfoProperties.getHost())) {
            String url = request.getRequestURL().toString();
            String host = url.substring(0, url.indexOf(request.getRequestURI()));
            //移除掉端口号
            host = host.split(":")[0];
            serverInfoProperties.setHost(host);
        }
        String uid = null;
        if (StringUtils.isNotEmpty(callback)) {
            //保存到redis中。用于回调的跳转
            uid = UUID.randomUUID().toString();
            String key = MagicValueStore.wxStatesValuePro + uid;
            stringRedisTemplate.opsForValue().append(key, callback);
            //此信息是10分钟后过期
            stringRedisTemplate.expire(key, 10, TimeUnit.MINUTES);
        }
        //首先获取当前登陆用户的openid，如果在数据库中有（在首次用户关注的时候就应该保存到数据库中），就不去获取用户的详细信息了。
        String url = wxMpService.oauth2buildAuthorizationUrl(serverInfoProperties.getHost() + "/wechat/auth/back/info", "snsapi_base", uid);
        //将微信网页从定向到登陆
        response.sendRedirect(url);
    }

    /**
     * 用户登录
     * @param loginSysUserDTO
     * @return
     */
    @ApiOperation("用户名密码登陆并绑定")
    @PostMapping("/login")
    @AutoErrorHandler
    public Result login(@RequestBody @Validated LoginSysUserDTO loginSysUserDTO, BindingResult bindingResult,HttpServletRequest request) throws WxErrorException {
        //绑定手机号
        String openId = (String)request.getSession().getAttribute("openId");
        if (StringUtils.isEmpty(openId)) {
            throw new BusinessException("来源错误！");
        }
        Optional<WxUser> wxUserOptional = wxUserService.findByOpenId(openId);
        EmptyUtils.assertOptional(wxUserOptional,"未能获取到您的信息!");
        WxUser wxUser = wxUserOptional.get();
        EmptyUtils.assertObjectNotNull(wxUser.getSysUser(),"此微信:"+wxUser.getNickname()+"已绑定账号!");

        //用户名密码登陆
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(loginSysUserDTO.getLoginName(),loginSysUserDTO.getPassword(),ContextUtils.getIpAddr()));
        LoginUserBean loginUser = (LoginUserBean) subject.getPrincipal();
        SysUser sysUser = sysUserService.findById(loginUser.getId());
        loginUser = AccountUtils.toLoginBean(sysUser);
        //覆盖掉session中存储的用户
        subject.getSession().setAttribute(LoginUserUtils.SESSION_USER_KEY,loginUser);
        subject.getSession().setAttribute(LoginUserUtils.LOGIN_USER_BROWSER,RequestUtils.getOsAndBrowserInfo());
        //默认可以绑定多个微信号，所以在此不做判断
        // 绑定本微信
        wxUser.setSysUser(sysUser);
        wxUserService.saveOrUpdate(wxUser);
        sysUserService.updateSubWechat(sysUser.getId(),true);
        //同步用户标签
        wxUserService.syncUser(sysUser,wxUser.getOpenId());

        return Result.getSuccess(loginUser);
    }
}
