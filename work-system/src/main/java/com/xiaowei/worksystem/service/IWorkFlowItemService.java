package com.xiaowei.worksystem.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.flow.WorkFlowItem;

import java.util.List;


public interface IWorkFlowItemService extends IBaseService<WorkFlowItem> {

    List<WorkFlowItem> findByWorkFlowId(String workFlowId);
}
