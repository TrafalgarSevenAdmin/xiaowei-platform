package com.xiaowei.worksystem.utils;

import com.xiaowei.worksystem.status.ServiceItemStatus;

public class ServiceItemUtils {

    //服务项目确认是否已经完成的状态标志数组
    public static final Integer[] isDone = {ServiceItemStatus.INEXECUTION.getStatus(),
            ServiceItemStatus.PAIED.getStatus(),
            ServiceItemStatus.COMPLETED.getStatus()};
    public static final Integer[] isNormal = {ServiceItemStatus.NORMAL.getStatus()};
}
