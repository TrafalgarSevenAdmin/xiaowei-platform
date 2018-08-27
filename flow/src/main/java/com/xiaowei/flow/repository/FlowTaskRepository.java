package com.xiaowei.flow.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.flow.entity.FlowTask;

public interface FlowTaskRepository extends BaseRepository<FlowTask> {


    FlowTask findByCode(String code);
}
