package com.xiaowei.account.consts;


import com.xiaowei.accountcommon.LoginUserBean;
import com.xiaowei.accountcommon.RoleBean;

import java.util.Arrays;
import java.util.Collections;

public class AccountConst {

    //在线用户列表，redis键
    public final static String ON_LINE_USER_KEY = "SYSTEM:USERS:ON_LINE_USER_KEY";

    //用户cookie分组前缀
    public final static String USER_REDIS_GROUP_PREFIX = "SYSTEM:USERS:LOGINED:";

    // 注册时默认分配的用户角色
    public final static String REGISTER_ROLE_CODE = "PTYH";

//    访客用户名，应该奇葩一点，防止被用户注册到这个用户名
    public final static String GUEST_USER_NAME = "GUEST_@*!^(*@!)^&$^(!@*!#^(!@*()@?#$!@@&T^(&^!$*(&()!#&()^&";

//    访客信息
    public final static LoginUserBean GUEST_USER_INFO = new LoginUserBean(
            "guest",
            "guest",
            null,
            null,
            "访客",
            UserStatus.NORMAL.getStatus(),
            Collections.EMPTY_LIST,
            Collections.EMPTY_LIST,
            null,
            null,
            null
    );

}
