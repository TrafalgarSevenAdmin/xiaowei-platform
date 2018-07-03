package com.xiaowei.account.service;


import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.basic.service.IBaseService;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 系统用户服务
 * @Version 1.0
 */
public interface ISysUserService extends IBaseService<SysUser> {

    SysUser saveUser(SysUser user);

    SysUser updateUser(SysUser user);

    void fakeDeleteUser(String userId);

    SysUser updateStatus(SysUser user);

    /**
     * 根据用户名查询用户
     * @param loginName
     * @return
     */
    SysUser findByLoginName(String loginName);
}
