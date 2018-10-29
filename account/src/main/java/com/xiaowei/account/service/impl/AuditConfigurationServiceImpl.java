package com.xiaowei.account.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.account.entity.AuditConfiguration;
import com.xiaowei.account.repository.AuditConfigurationRepository;
import com.xiaowei.account.service.IAuditConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class AuditConfigurationServiceImpl extends BaseServiceImpl<AuditConfiguration> implements IAuditConfigurationService {

    @Autowired
    private AuditConfigurationRepository auditConfigurationRepository;


    public AuditConfigurationServiceImpl(@Qualifier("auditConfigurationRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public AuditConfiguration saveAuditConfiguration(AuditConfiguration auditConfiguration) {
        auditConfiguration.setId(null);
        auditConfiguration.setCreatedTime(new Date());
        //判断是否已经存在该审核配置
        AuditConfiguration one = auditConfigurationRepository.findByUserIdAndDepartmentIdAndTypeStatus(auditConfiguration.getUserId(), auditConfiguration.getDepartmentId(), auditConfiguration.getTypeStatus());
        if (one != null) {
            throw new BusinessException("审核配置重复");
        }
        auditConfigurationRepository.save(auditConfiguration);
        return auditConfiguration;
    }

    @Override
    @Transactional
    public void deleteAuditConfiguration(String auditConfigurationId) {
        EmptyUtils.assertString(auditConfigurationId, "没有传入对象id");
        Optional<AuditConfiguration> optional = auditConfigurationRepository.findById(auditConfigurationId);
        EmptyUtils.assertOptional(optional, "没有查询到需要删除的对象");
        auditConfigurationRepository.delete(optional.get());
    }

    @Override
    @Transactional
    public void saveAllAuditConfiguration(List<AuditConfiguration> auditConfigurations) {
        auditConfigurations.stream().forEach(auditConfiguration -> {
            auditConfiguration.setId(null);
            auditConfiguration.setCreatedTime(new Date());
            //判断是否已经存在该审核配置
            AuditConfiguration one = auditConfigurationRepository.findByUserIdAndDepartmentIdAndTypeStatus(auditConfiguration.getUserId(), auditConfiguration.getDepartmentId(), auditConfiguration.getTypeStatus());
            if (one != null) {
                throw new BusinessException("审核配置重复");
            }
        });
        auditConfigurationRepository.saveAll(auditConfigurations);

    }
}
