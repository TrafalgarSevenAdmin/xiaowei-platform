package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.WorkOrderType;
import com.xiaowei.worksystem.repository.WorkOrderTypeRepository;
import com.xiaowei.worksystem.service.IWorkOrderTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;



@Service
public class WorkOrderTypeServiceImpl extends BaseServiceImpl<WorkOrderType> implements IWorkOrderTypeService {

    @Autowired
    private WorkOrderTypeRepository workOrderTypeRepository;

    public WorkOrderTypeServiceImpl(@Qualifier("workOrderTypeRepository")BaseRepository repository) {
        super(repository);
    }

}
