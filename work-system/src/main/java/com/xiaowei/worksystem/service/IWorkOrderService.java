package com.xiaowei.worksystem.service;


import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.EngineerWork;
import com.xiaowei.worksystem.entity.WorkOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface IWorkOrderService extends IBaseService<WorkOrder> {

    WorkOrder saveWorkOrder(WorkOrder workOrder, String workFlowId);

    WorkOrder updateWorkOrder(WorkOrder workOrder, String workFlowId);


    WorkOrder confirmed(String workOrderId, List<String> serviceItemIds);

    WorkOrder payServiceItem(String workOrderId);

    WorkOrder receivedWorkOrder(String workOrderId, Boolean receive);

    WorkOrder appointingWorkOrder(String workOrderId, Date appointingTime);

    WorkOrder departeWorkOrder(String workOrderId, Geometry shape);

    WorkOrder inhandWorkOrder(String workOrderId, Geometry shape, String arriveFileStore, Integer arriveStatus);

    WorkOrder finishInhand(String workOrderId);

    WorkOrder distributeWorkOrder(WorkOrder workOrder);

//    WorkOrder prePigeonhole(String workOrderId);

    WorkOrder pigeonholed(String workOrderId);

    String createPay(String workOrderId);

    Map<String,Object> getCountFromEngineer(String userId);

    Map<String,Object> getCountFromProposer(String userId);

    Map<String,Object> getCountFromBackgrounder(String userId);

    WorkOrder expenseing(String workOrderCode);

    WorkOrder finishedExpense(String workOrderCode);

    void pigeonholedStatus(String engineerWorkId, Integer pigeonholedStatus);

    WorkOrder saveInWorkOrder(WorkOrder workOrder);

    WorkOrder inFinishInhand(EngineerWork engineerWork, String workOrderId);
}
