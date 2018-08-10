package com.xiaowei.worksystem.service;


import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.WorkOrder;

import java.util.List;


public interface IWorkOrderService extends IBaseService<WorkOrder> {

    WorkOrder saveWorkOrder(WorkOrder workOrder, String workFlowId);

    WorkOrder updateWorkOrder(WorkOrder workOrder, String workFlowId);


    WorkOrder confirmed(String workOrderId, List<String> serviceItemIds);

    WorkOrder payServiceItem(String workOrderId);

    WorkOrder receivedWorkOrder(String workOrderId, Boolean receive);

    WorkOrder appointingWorkOrder(String workOrderId);

    WorkOrder departeWorkOrder(String workOrderId, Geometry shape);

    WorkOrder inhandWorkOrder(String workOrderId, Geometry shape, String arriveFileStore, Integer arriveStatus);

    WorkOrder finishInhand(String workOrderId);

    WorkOrder distributeWorkOrder(WorkOrder workOrder);

    /**
     * 将工单变成待归档状态
     *
     * @param workOrderId
     */
    void workOrderToPrepigeonhole(String workOrderId);

    WorkOrder prePigeonhole(String workOrderId);

    WorkOrder pigeonholed(String workOrderId);

    String createPay(String workOrderId);
}
