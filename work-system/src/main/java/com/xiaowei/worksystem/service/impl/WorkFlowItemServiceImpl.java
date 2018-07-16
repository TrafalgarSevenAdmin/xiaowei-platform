package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.flow.WorkFlowItem;
import com.xiaowei.worksystem.repository.flow.WorkFlowItemRepository;
import com.xiaowei.worksystem.service.IWorkFlowItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class WorkFlowItemServiceImpl extends BaseServiceImpl<WorkFlowItem> implements IWorkFlowItemService {

    @Autowired
    private WorkFlowItemRepository workFlowItemRepository;

    public WorkFlowItemServiceImpl(@Qualifier("workFlowItemRepository") BaseRepository repository) {
        super(repository);
    }

}
