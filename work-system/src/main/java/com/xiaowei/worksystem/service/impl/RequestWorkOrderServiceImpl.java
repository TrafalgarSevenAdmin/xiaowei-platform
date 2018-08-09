package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.worksystem.entity.RequestWorkOrder;
import com.xiaowei.worksystem.repository.RequestWorkOrderRepository;
import com.xiaowei.worksystem.service.IRequestWorkOrderService;
import com.xiaowei.worksystem.status.RequestWorkOrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;


@Service
public class RequestWorkOrderServiceImpl extends BaseServiceImpl<RequestWorkOrder> implements IRequestWorkOrderService {

    @Autowired
    private RequestWorkOrderRepository requestWorkOrderRepository;

    public RequestWorkOrderServiceImpl(@Qualifier("requestWorkOrderRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public RequestWorkOrder saveRequestWorkOrder(RequestWorkOrder requestWorkOrder) {
        requestWorkOrder.setId(null);
        requestWorkOrder.setCreatedTime(new Date());
        requestWorkOrder.setStatus(RequestWorkOrderStatus.UNTREATED.getStatus());//未处理
        requestWorkOrderRepository.save(requestWorkOrder);
        return requestWorkOrder;
    }

    @Override
    @Transactional
    public RequestWorkOrder updateStatus(RequestWorkOrder requestWorkOrder) {
        EmptyUtils.assertString(requestWorkOrder.getId(),"没有传入对象id");
        Optional<RequestWorkOrder> optional = requestWorkOrderRepository.findById(requestWorkOrder.getId());
        EmptyUtils.assertOptional(optional,"没有查询到需要修改的对象");
        RequestWorkOrder one = optional.get();
        one.setStatus(requestWorkOrder.getStatus());
        return requestWorkOrderRepository.save(one);
    }
}
