package com.xiaowei.account.repository;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 系统用户仓库
 * @Version 1.0
 */
public interface SysUserRepository extends BaseRepository<SysUser> {


    /**
     * 根据用户名查询用户
     * @param loginName
     * @return
     */
    SysUser findByLoginName(String loginName);

    /**
     * 通过手机号查找用户
     * @param mobile
     * @return
     */
    Optional<SysUser> findByMobile(String mobile);

    @Query(value = "select u from SysUser u where u.company.id is not null ")
    List<SysUser> findFromCompanies();

    @Query("select u from SysUser u where u.id in ?1")
    List<SysUser> findByIds(Set<String> userIds);

    @Query("select u from SysUser u where u.company.id = ?1")
    List<SysUser> findByCompanyId(String companyId);

    @Query("update SysUser set subWechat = ?2 where id = ?1")
    @Modifying
    void updateSubWechat(String userId, Boolean subWechat);
}
