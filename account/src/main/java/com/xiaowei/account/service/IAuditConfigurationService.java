package com.xiaowei.account.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.account.entity.AuditConfiguration;

import java.util.List;

public interface IAuditConfigurationService extends IBaseService<AuditConfiguration> {

    AuditConfiguration saveAuditConfiguration(AuditConfiguration auditConfiguration);

    void deleteAuditConfiguration(String auditConfigurationId);

    void saveAllAuditConfiguration(List<AuditConfiguration> auditConfigurations);
}
