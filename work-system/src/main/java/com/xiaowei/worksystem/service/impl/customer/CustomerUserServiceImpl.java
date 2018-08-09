package com.xiaowei.worksystem.service.impl.customer;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.customer.CustomerUser;
import com.xiaowei.worksystem.repository.customer.CustomerUserRepository;
import com.xiaowei.worksystem.service.customer.ICustomerUserService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class CustomerUserServiceImpl extends BaseServiceImpl<CustomerUser> implements ICustomerUserService {

    public CustomerUserServiceImpl(@Qualifier("customerUserRepository") BaseRepository repository) {
        super(repository);
    }

}