package com.xiaowei.account.service;

import com.xiaowei.account.entity.SysConfig;
import com.xiaowei.core.basic.service.IBaseService;

public interface ISysConfigService extends IBaseService<SysConfig> {

    SysConfig findByCode(String code);
}