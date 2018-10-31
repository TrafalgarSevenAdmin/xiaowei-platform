package com.xiaowei.account.service;


import com.xiaowei.account.entity.SysPermission;
import com.xiaowei.core.basic.service.IBaseService;

import java.util.List;
import java.util.Set;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 权限服务
 * @Version 1.0
 */
public interface ISysPermissionService extends IBaseService<SysPermission> {

    SysPermission savePermission(SysPermission permission);

    SysPermission updatePermission(SysPermission permission);

    void deletePermission(String permissionId);

    List<String> findByRoleId(String roleId);

    List<SysPermission> findBySymbolIn(Set<String> symbols);
}
