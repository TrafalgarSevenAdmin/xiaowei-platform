package com.xiaowei.account.consts;


import com.xiaowei.accountcommon.LoginUserBean;

import java.util.Collections;

public class AccountConst {

    public final static String ON_LINE_USER_KEY = "ON_LINE_USER_KEY";

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
