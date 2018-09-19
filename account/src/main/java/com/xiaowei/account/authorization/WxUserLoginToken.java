package com.xiaowei.account.authorization;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 微信用户直接登录，不需要使用密码
 * @author qinyongliang
 */
public class WxUserLoginToken extends UsernamePasswordToken {

    private String host;
    private String loginName;

    public WxUserLoginToken(String loginName,String host) {
        this.loginName = loginName;
        this.host = host;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public Object getPrincipal() {
        return loginName;
    }

    @Override
    public Object getCredentials() {
        return "".toCharArray();
    }
}
