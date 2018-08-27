package com.xiaowei.flow.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.flow.entity.FlowTask;
import com.xiaowei.flow.repository.FlowTaskRepository;
import com.xiaowei.flow.service.IFlowTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class FlowTaskServiceImpl extends BaseServiceImpl<FlowTask> implements IFlowTaskService {

    @Autowired
    FlowTaskRepository flowTaskRepository;


    public FlowTaskServiceImpl(@Qualifier("flowTaskRepository") BaseRepository repository) {
        super(repository);
    }


    @Override
    public FlowTask findByCode(String code) {
        return flowTaskRepository.findByCode(code);
    }
}