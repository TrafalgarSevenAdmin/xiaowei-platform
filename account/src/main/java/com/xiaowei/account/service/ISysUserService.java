package com.xiaowei.account.service;


import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.basic.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 系统用户服务
 * @Version 1.0
 */
public interface ISysUserService extends IBaseService<SysUser> {

    SysUser saveUser(SysUser user);

    /**
     * 通过手机号查找用户
     * @param mobile
     * @return
     */
    Optional<SysUser> findByMobile(String mobile);

    SysUser updateUser(SysUser user);

    void fakeDeleteUser(String userId);

    SysUser updateStatus(SysUser user);

    /**
     * 注册用户
     */
    @Transactional
    SysUser registerUser(SysUser user);

    /**
     * 根据用户名查询用户
     * @param loginName
     * @return
     */
    SysUser findByLoginName(String loginName);

    List<SysUser> findFromCompanys();

    SysUser updatePassword(String userId, String oldPassword, String newPassword);
}
