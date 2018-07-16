package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.flow.WorkFlowWorkFlowNode;
import com.xiaowei.worksystem.repository.flow.WorkFlowWorkFlowNodeRepository;
import com.xiaowei.worksystem.service.IWorkFlowWorkFlowNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class WorkFlowWorkFlowNodeServiceImpl extends BaseServiceImpl<WorkFlowWorkFlowNode> implements IWorkFlowWorkFlowNodeService {

    @Autowired
    private WorkFlowWorkFlowNodeRepository workFlowWorkFlowNodeRepository;

    public WorkFlowWorkFlowNodeServiceImpl(@Qualifier("workFlowWorkFlowNodeRepository") BaseRepository repository) {
        super(repository);
    }

}
