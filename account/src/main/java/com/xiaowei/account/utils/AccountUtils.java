package com.xiaowei.account.utils;

import com.xiaowei.account.consts.SuperUser;
import com.xiaowei.account.entity.*;
import com.xiaowei.account.service.*;
import com.xiaowei.accountcommon.*;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.context.ContextUtils;
import com.xiaowei.core.utils.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.ArrayList;
import java.util.List;

public class AccountUtils {


    /**
     * 加载用户的缓存信息 包含用户本身信息,角色,权限
     * @return
     */
    public static void loadUser(){
        if(LoginUserUtils.isLogin()){
            ISysUserService sysUserService = ContextUtils.getApplicationContext().getBean(ISysUserService.class);
            Subject subject = SecurityUtils.getSubject();
            LoginUserBean loginUserBean = (LoginUserBean) subject.getPrincipal();
            SysUser sysUser = sysUserService.findById(loginUserBean.getId());
            loginUserBean = toLoginBean(sysUser);
            //覆盖掉session中存储的用户
            subject.getSession().setAttribute(LoginUserUtils.SESSION_USER_KEY,loginUserBean);
            subject.getSession().setAttribute(LoginUserUtils.LOGIN_USER_BROWSER,RequestUtils.getOsAndBrowserInfo());
        }
    }

    public static LoginUserBean toLoginBean(SysUser sysUser){
        List<RoleBean> roles = new ArrayList<>();
        List<PermissionBean> permissions = new ArrayList<>();
        //构建登录用户信息
        if(SuperUser.ADMINISTRATOR_NAME.equals(sysUser.getLoginName())){
            //如果是超级管理员则拥有所有的权限和角色
            List<SysRole> sysRoles = ContextUtils.getApplicationContext().getBean(ISysRoleService.class).findAll();
            roles.addAll(BeanCopyUtils.copyList(sysRoles, RoleBean.class));
            List<SysPermission> sysPermissions = ContextUtils.getApplicationContext().getBean(ISysPermissionService.class).findAll();
            permissions.addAll(BeanCopyUtils.copyList(sysPermissions, PermissionBean.class));
        }else{
            sysUser.getRoles().forEach(sysRole -> {
                roles.add(BeanCopyUtils.copy(sysRole, RoleBean.class));
                permissions.addAll(BeanCopyUtils.copyList(sysRole.getPermissions(), PermissionBean.class));
            });
        }

        LoginUserBean loginUserBean = new LoginUserBean(
                sysUser.getId(),
                sysUser.getLoginName(),
                sysUser.getMobile(),
                sysUser.getEmail(),
                sysUser.getNickName(),
                sysUser.getStatus(),
                roles,
                permissions,
                BeanCopyUtils.copy(sysUser.getCompany(), CompanyBean.class),
                BeanCopyUtils.copy( sysUser.getDepartment(), DepartmentBean.class),
                BeanCopyUtils.copy(sysUser.getPost(), PostBean.class)
        );
        return loginUserBean;
    }
}
