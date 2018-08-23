package com.xiaowei.flow.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.flow.entity.FlowNode;
import com.xiaowei.flow.repository.FlowNodeRepository;
import com.xiaowei.flow.service.IFlowNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Service
public class FlowNodeServiceImpl extends BaseServiceImpl<FlowNode> implements IFlowNodeService {

    @Autowired
    FlowNodeRepository flowNodeRepository;

    public FlowNodeServiceImpl(@Qualifier("flowNodeRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    public List<FlowNode> findAllNodes(String flowId) {
        return flowNodeRepository.findByFlow_id(flowId);
    }
}