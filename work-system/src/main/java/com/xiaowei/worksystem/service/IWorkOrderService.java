package com.xiaowei.worksystem.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.WorkOrder;

import java.util.List;


public interface IWorkOrderService extends IBaseService<WorkOrder> {

    WorkOrder saveWorkOrder(WorkOrder workOrder);

    WorkOrder updateWorkOrder(WorkOrder workOrder);

    void fakeDelete(String workOrderId);

    WorkOrder confirmed(String workOrderId,List<String> serviceItemIds);

    WorkOrder payServiceItem(String workOrderId);

    WorkOrder receivedWorkOrder(String workOrderId);
}
