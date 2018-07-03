package com.xiaowei.account.authorization;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * @author zhouyang
 * @Date 2017-07-30 13:32
 * @Description
 * @Version 1.0
 */
public class TokenSessionManager extends DefaultWebSessionManager {

    protected Serializable getSessionId(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse rep = (HttpServletResponse) servletResponse;
        String token = null;
        //用户登录
        if(StringUtils.isEmpty(token)){
            Cookie cookie = WebUtils.getCookie(req, "_s");
            if(cookie != null){
                token = cookie.getValue();
            }else{
                token = req.getParameter( "_s");
            }
        }
        return token;
    }
}
