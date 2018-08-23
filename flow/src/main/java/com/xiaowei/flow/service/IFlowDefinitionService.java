package com.xiaowei.flow.service;

import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.flow.entity.FlowDefinition;

public interface IFlowDefinitionService extends IBaseService<FlowDefinition> {

    FlowDefinition findByCode(String code);
}