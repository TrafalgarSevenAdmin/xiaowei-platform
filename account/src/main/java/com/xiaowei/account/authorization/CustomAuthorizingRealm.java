package com.xiaowei.account.authorization;

import com.google.common.collect.Lists;
import com.xiaowei.account.consts.AccountConst;
import com.xiaowei.account.consts.SuperUser;
import com.xiaowei.account.consts.UserStatus;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.account.utils.AccountUtils;
import com.xiaowei.accountcommon.*;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.context.ContextUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author mocker
 * @Date 2018-03-29 15:30:48
 * @Description 自定义Realm
 * @Version 1.0
 */
public class CustomAuthorizingRealm extends AuthorizingRealm {

    @Override
    public String getName() {
        return super.getName();
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if(principalCollection.getPrimaryPrincipal() instanceof LoginUserBean){
            LoginUserBean loginUserBean = LoginUserUtils.getLoginUser();
            //暂时放弃缓存
            if(SuperUser.ADMINISTRATOR_NAME.equals(loginUserBean.getLoginName())){
                //如果是超级管理员赋予所有权限和角色
                simpleAuthorizationInfo.addRole("*.*");
                simpleAuthorizationInfo.addStringPermission("*.*");
            }

            if(!CollectionUtils.isEmpty(loginUserBean.getPermissions())){
                for (PermissionBean permissionBean : loginUserBean.getPermissions()) {
                    if(StringUtils.isNotEmpty(permissionBean.getSymbol())){
                        simpleAuthorizationInfo.addStringPermission(permissionBean.getSymbol());
                    }
                    if(StringUtils.isNotEmpty(permissionBean.getPrecondition())){
                        simpleAuthorizationInfo.addStringPermissions(Arrays.asList(permissionBean.getPrecondition().split(",")));
                    }
                }
            }
            if(!CollectionUtils.isEmpty(loginUserBean.getRoles())) {
                simpleAuthorizationInfo.addRoles(
                        loginUserBean.getRoles().stream().map(roleBean -> roleBean.getName())
                                .collect(Collectors.toList())
                );
            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String loginName = (String) authenticationToken.getPrincipal();
        String password = new String((char[])authenticationToken.getCredentials());

        //若是微信登陆且为访客用户，就直接登陆并标识为访客用户
        if ((authenticationToken instanceof WxUserLoginToken) && AccountConst.GUEST_USER_NAME.equals(loginName)) {
            return new SimpleAuthenticationInfo(AccountConst.GUEST_USER_INFO, password, getName());
        }

        //判断用户是否存在
        SysUser sysUser = ContextUtils.getApplicationContext().getBean(ISysUserService.class).findByLoginName(loginName);
        if(sysUser == null || sysUser.getDelete_flag()){
            throw new UnknownAccountException("用户不存在!");
        }

        /**
         * 如果不是wx登录的请求，才判断密码是否正确，如果是微信直接登录，就不必判断
         */
        if (!(authenticationToken instanceof WxUserLoginToken)) {
            //判断密码是否正确
            String md5Password = DigestUtils.md5Hex( sysUser.getSalt() + password);
            if(!md5Password.equals(sysUser.getPassword())){
                throw new IncorrectCredentialsException("密码错误!");
            }
        }


        //判断是否禁用
        if(UserStatus.FORBIDDEN.getStatus().equals(sysUser.getStatus())){
            throw new LockedAccountException("用户已经被禁用请联系管理员!");
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo =  new SimpleAuthenticationInfo(AccountUtils.toLoginBean(sysUser),password,getName());
        return simpleAuthenticationInfo;
    }


}
