package com.xiaowei.pay.service.Impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.pay.entity.XwOrder;
import com.xiaowei.pay.repository.XwOrderRepository;
import com.xiaowei.pay.service.IOrderService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

@Service
public class OrderServiceImpl extends BaseServiceImpl<XwOrder> implements IOrderService {

    @Autowired
    private XwOrderRepository xwOrderRepository;

    public OrderServiceImpl(@Qualifier("xwOrderRepository") BaseRepository<XwOrder> repository) {
        super(repository);
    }

    @Override
    public Optional<XwOrder> getById(Serializable id) {
        return xwOrderRepository.findById(id);
    }
}
