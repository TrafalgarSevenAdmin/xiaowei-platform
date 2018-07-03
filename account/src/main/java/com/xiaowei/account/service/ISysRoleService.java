package com.xiaowei.account.service;


import com.xiaowei.account.entity.SysRole;
import com.xiaowei.core.basic.service.IBaseService;

/**
 * @author mocker
 * @Date 2018-03-21 15:37:22
 * @Description 系统角色服务
 * @Version 1.0
 */
public interface ISysRoleService extends IBaseService<SysRole> {

    SysRole saveRole(SysRole role);

    SysRole updateRole(SysRole role);

    void deleteRole(String roleId);

}
