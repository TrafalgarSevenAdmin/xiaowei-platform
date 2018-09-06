package com.xiaowei.account.repository;

import com.xiaowei.account.entity.SysRole;
import com.xiaowei.core.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * @author mocker
 * @Date 2018-03-21 15:40:25
 * @Description 角色仓库
 * @Version 1.0
 */
public interface SysRoleRepository extends BaseRepository<SysRole> {



    SysRole findByCode(String code);

    SysRole findByName(String name);


    @Modifying
    @Query(value = "INSERT INTO sys_role_permission(role_id,permission_id) VALUES ( :roleId, :permissionId)", nativeQuery = true)
    void saveRolePermission(@Param("roleId") String roleId, @Param("permissionId") String permissionId);

    @Query("select r from SysRole r where r.code in :codes")
    List<SysRole> findByCodeIn(@Param("codes") Set<String> codes);

    @Modifying
    @Query(value = "delete from sys_user_role where ROLE_ID = ?1",nativeQuery = true)
    void deleteUserRoleByRoleId(String roleId);
}
