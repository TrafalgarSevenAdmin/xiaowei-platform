package com.xiaowei.account.repository;


import com.xiaowei.account.entity.Tenement;
import com.xiaowei.core.basic.repository.BaseRepository;

public interface TenementRepository extends BaseRepository<Tenement> {

    Tenement findByCode(String code);
}
