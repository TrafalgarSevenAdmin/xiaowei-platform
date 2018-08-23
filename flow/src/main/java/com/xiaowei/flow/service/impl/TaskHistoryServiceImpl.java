package com.xiaowei.flow.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.flow.entity.FlowTaskExecuteHistory;
import com.xiaowei.flow.service.ITaskHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class TaskHistoryServiceImpl extends BaseServiceImpl<FlowTaskExecuteHistory> implements ITaskHistoryService {

    public TaskHistoryServiceImpl(@Qualifier("taskHistoryRepository") BaseRepository repository) {
        super(repository);
    }

}