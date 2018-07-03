package com.xiaowei.account.service;


import com.xiaowei.account.entity.Company;
import com.xiaowei.core.basic.service.IBaseService;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 系统用户服务
 * @Version 1.0
 */
public interface ICompanyService extends IBaseService<Company> {
    Company saveCompany(Company company);

    Company updateCompany(Company company);

    void deleteCompany(String companyId);

    Company updateStatus(Company company);
}
