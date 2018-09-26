package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.worksystem.entity.Evaluate;
import com.xiaowei.worksystem.entity.WorkOrder;
import com.xiaowei.worksystem.repository.EvaluateRepository;
import com.xiaowei.worksystem.repository.WorkOrderRepository;
import com.xiaowei.worksystem.service.IEvaluateService;
import com.xiaowei.worksystem.status.ServiceType;
import com.xiaowei.worksystem.status.WorkOrderSystemStatus;
import com.xiaowei.worksystem.status.WorkOrderUserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class EvaluateServiceImpl extends BaseServiceImpl<Evaluate> implements IEvaluateService {

    @Autowired
    private EvaluateRepository evaluateRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository;

    public EvaluateServiceImpl(@Qualifier("evaluateRepository") BaseRepository repository) {
        super(repository);
    }


    @Override
    @Transactional
    public Evaluate saveEvaluate(String workOrderId, Evaluate evaluate) {
        WorkOrder workOrder = workOrderRepository.getOne(workOrderId);
        if (!workOrder.getUserStatus().equals(WorkOrderUserStatus.EVALUATED.getStatus())) {
            throw new BusinessException("工单不是待评价状态!");
        }
        if (!ServiceType.OUT.equals(workOrder.getWorkOrderType().getServiceType())) {
            throw new BusinessException("该工单类型非外部工单!");
        }
        evaluate.setCreatedTime(new Date());
        workOrder.setEvaluate(evaluateRepository.save(evaluate));
        workOrder.setUserStatus(WorkOrderUserStatus.NORMAO.getStatus());//修改为正常
        workOrderRepository.save(workOrder);
        return evaluate;
    }

    /**
     * 内部工单添加评价
     *
     * @param workOrderId
     * @param evaluate
     * @return
     */
    @Override
    @Transactional
    public Evaluate saveInEvaluate(String workOrderId, Evaluate evaluate) {
        WorkOrder workOrder = workOrderRepository.getOne(workOrderId);
        if (!workOrder.getSystemStatus().equals(WorkOrderSystemStatus.FINISHHAND.getStatus())) {
            throw new BusinessException("工单不是完成状态!");
        }
        if (!ServiceType.IN.equals(workOrder.getWorkOrderType().getServiceType())) {
            throw new BusinessException("该工单类型非内部工单!");
        }
        evaluate.setCreatedTime(new Date());
        workOrder.setEvaluate(evaluateRepository.save(evaluate));
        workOrder.setSystemStatus(WorkOrderSystemStatus.PIGEONHOLED.getStatus());//归档
        workOrderRepository.save(workOrder);
        return evaluate;
    }
}
