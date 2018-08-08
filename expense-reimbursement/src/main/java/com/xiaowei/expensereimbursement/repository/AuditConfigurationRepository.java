package com.xiaowei.expensereimbursement.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.expensereimbursement.entity.AuditConfiguration;
import org.springframework.data.jpa.repository.Query;

public interface AuditConfigurationRepository extends BaseRepository<AuditConfiguration>{
    @Query("select a from AuditConfiguration a where a.userId = ?1 and " +
            "a.departmentId = ?2 and a.typeStatus = ?3")
    AuditConfiguration findByUserIdAndDepartmentIdAndTypeStatus(String userId, String departmentId, Integer typeStatus);
}
