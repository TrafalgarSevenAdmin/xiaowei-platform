package com.xiaowei.worksystem.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.flow.WorkFlow;


public interface IWorkFlowService extends IBaseService<WorkFlow> {

    WorkFlow saveWorkFlow(WorkFlow workFlow);

    WorkFlow updateWorkFlow(WorkFlow workFlow);

    void deleteWorkFlow(String workFlowId);

    WorkFlow updateStatus(WorkFlow workFlow);
}
