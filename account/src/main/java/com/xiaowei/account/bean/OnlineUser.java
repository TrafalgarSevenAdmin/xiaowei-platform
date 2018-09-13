package com.xiaowei.account.bean;

import com.xiaowei.accountcommon.*;
import lombok.Data;
import org.apache.shiro.session.Session;

import java.util.Date;

import static com.xiaowei.accountcommon.LoginUserUtils.LOGIN_USER_BROWSER;
import static com.xiaowei.accountcommon.LoginUserUtils.SESSION_USER_KEY;

/**
 * 登陆用户
 */
@Data
public class OnlineUser {

    public OnlineUser(Session session) {
        this.id = (String) session.getId();
        this.loginTime = session.getStartTimestamp();
        this.lastAccessTime = session.getLastAccessTime();
        this.host = session.getHost();
        LoginUserBean user = (LoginUserBean) session.getAttribute(SESSION_USER_KEY);
        this.userId = user.getId();
        this.loginName = user.getLoginName();
        this.mobile = user.getMobile();
        this.email = user.getEmail();
        this.nickName = user.getNickName();
        this.status = user.getStatus();
        this.browser = (String) session.getAttribute(LOGIN_USER_BROWSER);
    }

    public String id;

    public Date loginTime;

    public Date lastAccessTime;

    /**
     * 登陆ip
     */
    public String host;

    public String userId;

    private String loginName;

    private String mobile;

    private String email;

    private String nickName;

    private Integer status;

    private String browser;
}
