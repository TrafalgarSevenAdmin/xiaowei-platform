package com.xiaowei.expensereimbursement.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.expensereimbursement.entity.AuditConfiguration;

public interface IAuditConfigurationService extends IBaseService<AuditConfiguration> {

    AuditConfiguration saveAuditConfiguration(AuditConfiguration auditConfiguration);

    void deleteAuditConfiguration(String auditConfigurationId);
}
