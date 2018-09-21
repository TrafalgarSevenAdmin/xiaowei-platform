package com.xiaowei.worksystem.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.Evaluate;


public interface IEvaluateService extends IBaseService<Evaluate> {

    Evaluate saveEvaluate(String workOrderId, Evaluate evaluate);

    Evaluate saveInEvaluate(String workOrderId, Evaluate evaluate);
}
