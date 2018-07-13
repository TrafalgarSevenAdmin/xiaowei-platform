package com.xiaowei.account.service;


import com.xiaowei.account.entity.Company;
import com.xiaowei.core.basic.service.IBaseService;

public interface ICompanyService extends IBaseService<Company> {
    Company saveCompany(Company company);

    Company updateCompany(Company company);

    void deleteCompany(String companyId);

    Company updateStatus(Company company);
}
