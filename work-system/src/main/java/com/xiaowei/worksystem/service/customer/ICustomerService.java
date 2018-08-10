package com.xiaowei.worksystem.service.customer;

import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.customer.Customer;

import java.util.List;

public interface ICustomerService extends IBaseService<Customer> {

    List<String> getCountys();

    List<Customer> getCustomerByCountys(String county);
}