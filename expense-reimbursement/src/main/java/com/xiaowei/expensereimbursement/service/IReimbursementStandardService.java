package com.xiaowei.expensereimbursement.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.expensereimbursement.entity.ReimbursementStandard;

public interface IReimbursementStandardService extends IBaseService<ReimbursementStandard> {

    ReimbursementStandard saveReimbursementStandard(ReimbursementStandard reimbursementStandard);

    void deleteReimbursementStandard(String standardId);

    ReimbursementStandard updateReimbursementStandard(ReimbursementStandard reimbursementStandard);
}