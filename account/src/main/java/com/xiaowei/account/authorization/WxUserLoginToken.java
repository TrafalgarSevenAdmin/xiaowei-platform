package com.xiaowei.account.authorization;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 微信用户直接登录，不需要使用密码
 * @author qinyongliang
 */
public class WxUserLoginToken implements AuthenticationToken {

    private String loginName;

    public WxUserLoginToken(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public Object getPrincipal() {
        return loginName;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
