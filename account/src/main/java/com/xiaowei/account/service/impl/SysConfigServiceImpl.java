package com.xiaowei.account.service.impl;

import com.xiaowei.account.entity.SysConfig;
import com.xiaowei.account.repository.SysConfigRepository;
import com.xiaowei.account.service.ISysConfigService;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Serializable;

@Service
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfig> implements ISysConfigService {

    @Autowired
    SysConfigRepository sysConfigRepository;

    public SysConfigServiceImpl(@Qualifier("sysConfigRepository") BaseRepository repository) {
        super(repository);
    }

    /**
     * 保存或更新
     * 同时在更新时清除此记录曾经的code的缓存
     * @param sysConfig
     * @return
     */
    @Override
    @CacheEvict(value = "sysConfig", key = "(#sysConfig.id!=null and #sysConfig.id!='')?@sysConfigServiceImpl.findById(#sysConfig.id).code:''",beforeInvocation = true)
    public SysConfig save(SysConfig sysConfig) {
        return super.save(sysConfig);
    }

    /**
     * 删除
     * 同时清除此code的缓存
     * @param id
     */
    @Override
    @CacheEvict(value = "sysConfig", key = "@sysConfigServiceImpl.findById(#id).code",beforeInvocation = true)
    public void delete(Serializable id) {
        super.delete(id);
    }


    /**
     * 根据code获取配置
     * 不缓存空值，防止错误
     * @param code
     * @return
     */
    @Override
    @Cacheable(value = "sysConfig", key = "#code",unless="#result == null")
    public SysConfig findByCode(String code) {
        return sysConfigRepository.findByCode(code);
    }
}