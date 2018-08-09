package com.xiaowei.worksystem.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.RequestWorkOrder;


public interface IRequestWorkOrderService extends IBaseService<RequestWorkOrder> {

    RequestWorkOrder saveRequestWorkOrder(RequestWorkOrder requestWorkOrder);

    RequestWorkOrder updateStatus(RequestWorkOrder requestWorkOrder);
}
