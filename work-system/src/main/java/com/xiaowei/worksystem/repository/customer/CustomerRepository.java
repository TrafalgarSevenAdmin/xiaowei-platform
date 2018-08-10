package com.xiaowei.worksystem.repository.customer;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.worksystem.entity.customer.Customer;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends BaseRepository<Customer> {

    @Query("select c.county from Customer c group by c.county")
    List getCountys();

    List<Customer> findByCounty(String county);
}


