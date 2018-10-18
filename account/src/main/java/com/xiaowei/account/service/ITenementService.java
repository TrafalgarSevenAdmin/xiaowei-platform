package com.xiaowei.account.service;


import com.xiaowei.account.entity.Tenement;
import com.xiaowei.core.basic.service.IBaseService;

public interface ITenementService extends IBaseService<Tenement> {

    Tenement saveTenement(Tenement tenement);

    Tenement updateTenement(Tenement tenement);

    Tenement updateStatus(Tenement tenement);
}
