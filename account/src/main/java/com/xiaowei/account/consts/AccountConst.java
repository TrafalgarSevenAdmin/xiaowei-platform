package com.xiaowei.account.consts;


import com.xiaowei.accountcommon.LoginUserBean;
import com.xiaowei.accountcommon.RoleBean;

import java.util.Arrays;
import java.util.Collections;

public class AccountConst {

    public final static String ON_LINE_USER_KEY = "ON_LINE_USER_KEY";

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
            // 由于设计用户注册时，其
            Arrays.asList(RoleBean.builder().id("3").build()),
            Collections.EMPTY_LIST,
            null,
            null,
            null
    );

}
