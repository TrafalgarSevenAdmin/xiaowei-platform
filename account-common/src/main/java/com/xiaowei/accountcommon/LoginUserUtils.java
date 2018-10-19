package com.xiaowei.accountcommon;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authz.UnauthenticatedException;

/**
 * 登录用户工具类
 */
public class LoginUserUtils {

    public static final String SESSION_USER_KEY = "LOGIN_USER";

    public static final String LOGIN_USER_BROWSER = "LOGIN_USER_BROWSER";

    /**
     * 获取当前登录的用户
     *
     * @return
     * @throws UnauthenticatedException
     */
    public static LoginUserBean getLoginUser() throws UnauthenticatedException {
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            throw new UnauthenticatedException("用户未登录!");
        } else {
            Object user = SecurityUtils.getSubject().getSession().getAttribute(SESSION_USER_KEY);
            return (LoginUserBean) user;
        }
    }

    /**
     * 获取当前登录的用户
     *
     * @return
     * @throws UnauthenticatedException
     */
    public static LoginUserBean getLoginUserOrNull() throws UnauthenticatedException {
        try {
            if (!SecurityUtils.getSubject().isAuthenticated()) {
                return null;
            } else {
                Object user = SecurityUtils.getSubject().getSession().getAttribute(SESSION_USER_KEY);
                return (LoginUserBean) user;
            }
        } catch (UnavailableSecurityManagerException exception) {
            return null;
        }
    }


    /**
     * 判断当前用户是否登录
     *
     * @return
     * @throws UnauthenticatedException
     */
    public static boolean isLogin() throws UnauthenticatedException {
        return SecurityUtils.getSubject().isAuthenticated();
    }

    /**
     * 当前用户是否有此角色Id
     *
     * @return
     * @throws UnauthenticatedException
     */
    public static boolean hasRoleId(String roleId) throws UnauthenticatedException {
        return getLoginUser().getRoles().stream()
                .filter(roleBean -> roleBean.getId().equals(roleId))
                .findAny()
                .isPresent();
    }

//    /**
//     * 判断是否有此公司
//     * @param companyId
//     * @return
//     * @throws UnauthenticatedException
//     */
//    public static boolean hasCompanyId(String companyId) throws UnauthenticatedException{
//        if("admin".equals(getLoginUser().getLoginName())){
//            return true;
//        }
//        return getLoginUser().getCompanyBean().getId().equals(companyId);
//    }

//    /**
//     * 判断是否有该部门
//     * @param departmentId
//     * @return
//     * @throws UnauthenticatedException
//     */
//    public static boolean hasDepartmentId(String departmentId) throws UnauthenticatedException{
//        if("admin".equals(getLoginUser().getLoginName())){
//            return true;
//        }
//        return getLoginUser().getDepartmentBean().getId().equals(departmentId);
//    }

    /**
     * 当前用户是否有此权限Id
     *
     * @return
     * @throws UnauthenticatedException
     */
    public static boolean hasPermissionId(String permissionId) throws UnauthenticatedException {
        return getLoginUser().getPermissions().stream()
                .filter(permissionBean -> permissionBean.getId().equals(permissionId))
                .findAny()
                .isPresent();
    }


}
