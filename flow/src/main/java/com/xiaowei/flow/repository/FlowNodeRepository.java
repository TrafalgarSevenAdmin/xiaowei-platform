package com.xiaowei.flow.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.flow.entity.FlowNode;

import java.util.List;

public interface FlowNodeRepository extends BaseRepository<FlowNode> {

    List<FlowNode> findByFlow_id(String flowId);
}
