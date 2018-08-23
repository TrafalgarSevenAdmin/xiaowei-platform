package com.xiaowei.flow.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.flow.entity.FlowDefinition;

public interface FlowDefinitionRepository extends BaseRepository<FlowDefinition> {

    FlowDefinition findByCode(String code);
}
