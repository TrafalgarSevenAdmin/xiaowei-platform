package com.xiaowei.flow.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.flow.entity.FlowDefinition;
import com.xiaowei.flow.entity.FlowNode;
import com.xiaowei.flow.repository.FlowDefinitionRepository;
import com.xiaowei.flow.service.IFlowDefinitionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;

@Service
public class FlowDefinitionServiceImpl extends BaseServiceImpl<FlowDefinition> implements IFlowDefinitionService {

    @Autowired
    FlowDefinitionRepository flowDefinitionRepository;

    public FlowDefinitionServiceImpl(@Qualifier("flowDefinitionRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    public FlowDefinition save(FlowDefinition flowDefinition){
        flowDefinition = flowDefinitionRepository.save(flowDefinition);
        FlowNode start = flowDefinition.getStart();
        ArrayList<FlowNode> subNodes = new ArrayList<>();
        subNodes.add(start);
        while (!subNodes.isEmpty()) {
            FlowNode flowNode = subNodes.get(0);
            subNodes.remove(0);
            flowNode.setFlowId(flowDefinition.getId());
            if(CollectionUtils.isNotEmpty(flowNode.getNextNodes())) subNodes.addAll(flowNode.getNextNodes());
        }
        //再保存一遍主要是为了设置所有子节点的id
        return flowDefinitionRepository.save(flowDefinition);
    }

    @Override
    public FlowDefinition update(FlowDefinition flowDefinition){
        return this.save(flowDefinition);
    }

    @Override
    public FlowDefinition findByCode(String code) {
        return flowDefinitionRepository.findByCode(code);
    }
}