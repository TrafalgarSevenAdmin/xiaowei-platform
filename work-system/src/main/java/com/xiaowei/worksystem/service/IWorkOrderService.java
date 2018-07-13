package com.xiaowei.worksystem.service;


import com.vividsolutions.jts.geom.Geometry;
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

    WorkOrder appointingWorkOrder(String workOrderId);

    WorkOrder departeWorkOrder(String workOrderId, Geometry shape);

    WorkOrder inhandWorkOrder(String workOrderId, Geometry shape);

    WorkOrder finishInhand(String workOrderId);

}
