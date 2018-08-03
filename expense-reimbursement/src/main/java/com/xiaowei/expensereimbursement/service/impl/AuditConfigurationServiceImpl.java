package com.xiaowei.expensereimbursement.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.expensereimbursement.entity.AuditConfiguration;
import com.xiaowei.expensereimbursement.repository.AuditConfigurationRepository;
import com.xiaowei.expensereimbursement.service.IAuditConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * 待修改的设备服务
 */
@Service
public class AuditConfigurationServiceImpl extends BaseServiceImpl<AuditConfiguration> implements IAuditConfigurationService {

    @Autowired
    private AuditConfigurationRepository auditConfigurationRepository;


    public AuditConfigurationServiceImpl(@Qualifier("auditConfigurationRepository")BaseRepository repository) {
        super(repository);
    }
}
