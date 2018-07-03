package com.xiaowei.account.repository;

import com.xiaowei.account.entity.SysPermission;
import com.xiaowei.core.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 权限仓库
 * @Version 1.0
 */
public interface SysPermissionRepository extends BaseRepository<SysPermission> {

    @Query("select p from SysPermission p where p.name = :name and (p.parentCode is null or p.parentCode = '')")
    List<SysPermission> findByNameAndParentCodeIsNull(@Param("name") String name);

    SysPermission findByCode(String authCode);

    @Query("select max(p.ownCode) from SysPermission p where p.parentCode = :parentCode")
    Long findMaxOwnCodeByParentCode(@Param("parentCode") String parentCode);

    @Query("select p from SysPermission p where p.name = :name and p.parentCode = :parentCode")
    List<SysPermission> findByNameAndParentCode(@Param("name") String name, @Param("parentCode") String parentCode);

    SysPermission findBySymbol(String symbol);

    @Modifying
    @Query("delete from SysPermission p where p.id = :permissionId")
    void deleteById(@Param("permissionId") String permissionId);

    @Modifying
    @Query("delete from SysPermission p where p.code like :code")
    void deleteByCodeLike(@Param("code") String code);

    @Query(value = "SELECT rp.PERMISSION_ID from sys_role_permission rp where rp.ROLE_ID = :roleId",nativeQuery=true)
    List<String> findByRoleId(@Param("roleId") String roleId);
}
