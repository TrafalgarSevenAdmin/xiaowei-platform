package com.xiaowei.account.repository;

import com.xiaowei.account.entity.SysConfig;
import com.xiaowei.core.basic.repository.BaseRepository;

public interface SysConfigRepository extends BaseRepository<SysConfig> {

    /**
     * 根据代码获取配置
     * @param code
     * @return
     */
    SysConfig findByCode(String code);
}
