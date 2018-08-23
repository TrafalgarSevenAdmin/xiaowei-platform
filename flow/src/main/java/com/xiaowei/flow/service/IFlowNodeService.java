package com.xiaowei.flow.service;

import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.flow.entity.FlowNode;

import java.util.List;

public interface IFlowNodeService extends IBaseService<FlowNode> {

    List<FlowNode> findAllNodes(String flowId);
}