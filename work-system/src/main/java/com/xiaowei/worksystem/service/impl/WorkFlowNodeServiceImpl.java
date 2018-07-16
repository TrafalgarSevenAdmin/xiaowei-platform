package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.flow.WorkFlowNode;
import com.xiaowei.worksystem.repository.flow.WorkFlowNodeRepository;
import com.xiaowei.worksystem.service.IWorkFlowNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class WorkFlowNodeServiceImpl extends BaseServiceImpl<WorkFlowNode> implements IWorkFlowNodeService {

    @Autowired
    private WorkFlowNodeRepository workFlowNodeRepository;

    public WorkFlowNodeServiceImpl(@Qualifier("workFlowNodeRepository") BaseRepository repository) {
        super(repository);
    }

}
