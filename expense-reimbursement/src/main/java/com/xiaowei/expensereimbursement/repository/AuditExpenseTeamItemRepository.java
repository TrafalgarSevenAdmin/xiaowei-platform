package com.xiaowei.expensereimbursement.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.expensereimbursement.entity.AuditExpenseTeamItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AuditExpenseTeamItemRepository extends BaseRepository<AuditExpenseTeamItem>{

    @Modifying
    @Query("delete from AuditExpenseTeamItem ai where ai.auditExpenseTeam.id = ?1")
    void deleteByTeamId(String teamId);

}
