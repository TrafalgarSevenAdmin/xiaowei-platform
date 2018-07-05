package com.xiaowei.worksystem.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.Workpiece;


public interface IWorkpieceService extends IBaseService<Workpiece> {


    void fakeDelete(String workpieceId);
}
