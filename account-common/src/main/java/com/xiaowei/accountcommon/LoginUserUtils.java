package com.xiaowei.accountcommon;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;

/**
 * 登录用户工具类
 */
public class LoginUserUtils {

    public static final String SESSION_USER_KEY = "LOGIN_USER";

    /**
     * 获取当前登录的用户
     * @return
     * @throws UnauthenticatedException
     */
    public static LoginUserBean getLoginUser() throws UnauthenticatedException{
        if(!SecurityUtils.getSubject().isAuthenticated()) {
            throw new UnauthenticatedException("用户未登录!");
        }else{
            Object user = SecurityUtils.getSubject().getSession().getAttribute(SESSION_USER_KEY);
            return (LoginUserBean) user;
        }
    }


    /**
     * 判断当前用户是否登录
     * @return
     * @throws UnauthenticatedException
     */
    public static boolean isLogin() throws UnauthenticatedException{
        return SecurityUtils.getSubject().isAuthenticated();
    }

    /**
     * 当前用户是否有此角色Id
     * @return
     * @throws UnauthenticatedException
     */
    public static boolean hasRoleId(String roleId) throws UnauthenticatedException{
        return getLoginUser().getRoles().stream()
                .filter(roleBean ->roleBean.getId().equals(roleId))
                .findAny()
                .isPresent();
    }

    /**
     * 判断是否有次公司
     * @param companyId
     * @return
     * @throws UnauthenticatedException
     */
    public static boolean hasCompanyId(String companyId) throws UnauthenticatedException{
        return getLoginUser().getCompanyBeans().stream()
                .filter(companyBean -> companyBean.getId().equals(companyId))
                .findAny()
                .isPresent();
    }

    /**
     * 当前用户是否有此权限Id
     * @return
     * @throws UnauthenticatedException
     */
    public static boolean hasPermissionId(String permissionId) throws UnauthenticatedException{
       return getLoginUser().getPermissions().stream()
               .filter(permissionBean -> permissionBean.getId().equals(permissionId))
               .findAny()
               .isPresent();
    }


}
