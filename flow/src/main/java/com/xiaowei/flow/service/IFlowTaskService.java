package com.xiaowei.flow.service;

import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.flow.entity.FlowTask;

public interface IFlowTaskService extends IBaseService<FlowTask> {

    /**
     * 根据任务代码搜索
     * @param code
     * @return
     */
    FlowTask findByCode(String code);
}