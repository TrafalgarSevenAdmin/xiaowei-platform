package com.xiaowei.account.repository;


import com.xiaowei.account.entity.Company;
import com.xiaowei.core.basic.repository.BaseRepository;

public interface CompanyRepository extends BaseRepository<Company> {
    Company findByCompanyName(String companyName);
}
