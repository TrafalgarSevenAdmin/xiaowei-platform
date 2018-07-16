package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.flow.WorkFlow;
import com.xiaowei.worksystem.repository.flow.WorkFlowRepository;
import com.xiaowei.worksystem.service.IWorkFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class WorkFlowServiceImpl extends BaseServiceImpl<WorkFlow> implements IWorkFlowService {

    @Autowired
    private WorkFlowRepository workFlowRepository;

    public WorkFlowServiceImpl(@Qualifier("workFlowRepository") BaseRepository repository) {
        super(repository);
    }

}
