package com.xiaowei.account.consts;


import com.xiaowei.accountcommon.LoginUserBean;

import java.util.Arrays;
import java.util.Collections;

public class AccountConst {

    //在线用户列表，redis键
    public final static String ON_LINE_USER_KEY = "SYSTEM:USERS:ON_LINE_USER_KEY";
    public static String ADMIN_TENENCYID = "1";

    //用户cookie分组前缀
    public final static String USER_REDIS_GROUP_PREFIX = "SYSTEM:USERS:LOGINED:";

    //验证码分租前缀
    public final static String VERIFICATION_CODE_REDIS_GROUP_PREFIX = "SYSTEM:USERS:VERIFICATION:";

    public final static String USER_DEFAULT_PASSWORD = "123456";

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
            //默认拥有查询所有租户的权限，以便于在注册时选择公司
            Arrays.asList("account:tenement:query"),
            null,
            null,
            null,
        null
    );

}
