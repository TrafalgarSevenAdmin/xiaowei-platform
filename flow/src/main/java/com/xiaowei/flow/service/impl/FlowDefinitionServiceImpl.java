package com.xiaowei.flow.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.flow.entity.FlowDefinition;
import com.xiaowei.flow.entity.FlowNode;
import com.xiaowei.flow.repository.FlowDefinitionRepository;
import com.xiaowei.flow.service.IFlowDefinitionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
        Set<String> codes = new HashSet<>();
        while (!subNodes.isEmpty()) {
            FlowNode flowNode = subNodes.get(0);
            subNodes.remove(0);
            //判定流程代码不能为空和不能重复
            Assert.isTrue(StringUtils.isNotEmpty(flowNode.getCode()), "流程节点"+flowNode.getName()+"的流程代码不能为空");
            if (codes.contains(flowNode.getCode())) {
                throw new BusinessException("流程代码（" + flowNode.getCode() + "）重复");
            }
            codes.add(flowNode.getCode());
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