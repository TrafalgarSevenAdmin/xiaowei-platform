package com.xiaowei.worksystem.utils;

import com.xiaowei.worksystem.status.WorkOrderSystemStatus;

public class WorkOrderUtils {
    //工程师待接单
    public static final Integer[] RECEIVE = {WorkOrderSystemStatus.RECEIVE.getStatus()};
    //可以取消的状态
    public static final Integer[] CANCANCEL = {WorkOrderSystemStatus.DISTRIBUTE.getStatus(),
            WorkOrderSystemStatus.RECEIVE.getStatus(),
            WorkOrderSystemStatus.APPOINTING.getStatus(),
            WorkOrderSystemStatus.DEPART.getStatus()};
    //工程师处理中
    public static final Integer[] INHAND = {WorkOrderSystemStatus.APPOINTING.getStatus(),
            WorkOrderSystemStatus.DEPART.getStatus(),
            WorkOrderSystemStatus.INHAND.getStatus(),
            WorkOrderSystemStatus.QUALITY.getStatus(),
            WorkOrderSystemStatus.TRIPING.getStatus()};

    //工程师处理完成
    public static final Integer[] FINISHED = {WorkOrderSystemStatus.FINISHHAND.getStatus(),
            WorkOrderSystemStatus.CANCEL.getStatus(),
            WorkOrderSystemStatus.EXPENSEING.getStatus(),
            WorkOrderSystemStatus.PREPIGEONHOLE.getStatus()};
    //派单员已派发
    public static final Integer[] DISTRIBUTED = {WorkOrderSystemStatus.RECEIVE.getStatus(),
            WorkOrderSystemStatus.APPOINTING.getStatus(),
            WorkOrderSystemStatus.DEPART.getStatus(),
            WorkOrderSystemStatus.TRIPING.getStatus(),
            WorkOrderSystemStatus.INHAND.getStatus(),
            WorkOrderSystemStatus.QUALITY.getStatus()};
}
