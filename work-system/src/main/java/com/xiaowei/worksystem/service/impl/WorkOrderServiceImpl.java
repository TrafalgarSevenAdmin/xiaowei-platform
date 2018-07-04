package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.WorkOrder;
import com.xiaowei.worksystem.repository.WorkOrderRepository;
import com.xiaowei.worksystem.service.IWorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class WorkOrderServiceImpl extends BaseServiceImpl<WorkOrder> implements IWorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    public WorkOrderServiceImpl(@Qualifier("workOrderRepository")BaseRepository repository) {
        super(repository);
    }


    @Override
    @Transactional
    public WorkOrder saveWorkOrder(WorkOrder workOrder) {
        return null;
    }

    @Override
    @Transactional
    public WorkOrder updateWorkOrder(WorkOrder workOrder) {
        return null;
    }
}
