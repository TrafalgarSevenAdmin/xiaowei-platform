package com.xiaowei.worksystem.service.impl.customer;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.customer.Customer;
import com.xiaowei.worksystem.repository.customer.CustomerRepository;
import com.xiaowei.worksystem.service.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer> implements ICustomerService {


    @Autowired
    private CustomerRepository customerRepository;

    public CustomerServiceImpl(@Qualifier("customerRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    public List<String> getCountys() {
        return null;
    }
}