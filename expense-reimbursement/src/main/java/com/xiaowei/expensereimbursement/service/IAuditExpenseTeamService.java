package com.xiaowei.expensereimbursement.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.expensereimbursement.entity.AuditExpenseTeam;


public interface IAuditExpenseTeamService extends IBaseService<AuditExpenseTeam> {

    AuditExpenseTeam saveAuditExpenseTeam(AuditExpenseTeam auditExpenseTeam);

    AuditExpenseTeam updateAuditExpenseTeam(AuditExpenseTeam auditExpenseTeam);

    void deleteAuditExpenseTeam(String teamId);
}
