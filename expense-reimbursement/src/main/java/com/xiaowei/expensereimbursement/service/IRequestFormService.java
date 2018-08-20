package com.xiaowei.expensereimbursement.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.expensereimbursement.entity.RequestForm;


public interface IRequestFormService extends IBaseService<RequestForm> {

    RequestForm saveRequestForm(RequestForm requestForm);

    RequestForm updateRequestForm(RequestForm requestForm);

    RequestForm audit(RequestForm requestForm, Boolean audit);
}
