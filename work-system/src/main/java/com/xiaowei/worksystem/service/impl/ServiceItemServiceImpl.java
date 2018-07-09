package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.worksystem.entity.ServiceItem;
import com.xiaowei.worksystem.entity.WorkOrder;
import com.xiaowei.worksystem.repository.ServiceItemRepository;
import com.xiaowei.worksystem.repository.WorkOrderRepository;
import com.xiaowei.worksystem.service.IServiceItemService;
import com.xiaowei.worksystem.status.ServiceItemSource;
import com.xiaowei.worksystem.status.ServiceItemStatus;
import com.xiaowei.worksystem.status.WorkOrderSystemStatus;
import com.xiaowei.worksystem.status.WorkOrderUserStatus;
import com.xiaowei.worksystem.utils.ServiceItemUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class ServiceItemServiceImpl extends BaseServiceImpl<ServiceItem> implements IServiceItemService {

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    public ServiceItemServiceImpl(@Qualifier("serviceItemRepository") BaseRepository repository) {
        super(repository);
    }


    /**
     * 工程师添加收费项目
     *
     * @param serviceItems
     * @return
     */
    @Override
    @Transactional
    public List<ServiceItem> saveByEngineer(String workOrderId, List<ServiceItem> serviceItems) {
        WorkOrder workOrder = workOrderRepository.getOne(workOrderId);
        Integer maxOrderNumber = serviceItemRepository.findMaxOrderNumberByWorkOrderId(workOrderId);
        if (maxOrderNumber == null) {
            maxOrderNumber = 0;
        }

        for (int i = 0; i < serviceItems.size(); i++) {
            ServiceItem serviceItem = serviceItems.get(i);
            EmptyUtils.assertObject(serviceItem.getCharge(), "是否收费必填");
            EmptyUtils.assertString(serviceItem.getServiceType(), "服务项目类型必填");
            serviceItem.setStatus(ServiceItemStatus.CONFIRMED.getStatus());//待确认状态
            serviceItem.setSource(ServiceItemSource.ENGINEER.getStatus());//工程师来源
            serviceItem.setAudit(true);//需要审核
            serviceItem.setWorkOrder(workOrder);//所属工单
            serviceItem.setOrderNumber(++maxOrderNumber);//顺序
            serviceItem.setCreatedTime(new Date());
            serviceItemRepository.save(serviceItem);
        }
        workOrder.setUserStatus(WorkOrderUserStatus.CONFIRMED.getStatus());//工单用户状态设置为待确认
        workOrderRepository.save(workOrder);
        return serviceItems;
    }

    /**
     * 工程师执行服务项目
     *
     * @param serviceItemId
     */
    @Override
    @Transactional
    public ServiceItem executeServiceItem(String serviceItemId) {
        Optional<ServiceItem> one = serviceItemRepository.findById(serviceItemId);
        EmptyUtils.assertOptional(one, "没有查询到需要执行的服务项目");
        ServiceItem serviceItem = one.get();
        //1.判断上一步是否已经做完
        judgeLateIsDone(serviceItem);
        //2.判断当前步状态是否正常
        judgeCurrentIsNormal(serviceItem);
        //3.判断当前步是否需要审核: 若需要审核,则变更状态为待审核且不执行第四部,若不审核则执行第四部
        Boolean isAudit = judgeCurrentIsAudit(serviceItem);
        //4.判断是否收费
        if (!isAudit) {
            judgeCurrentIsCharge(serviceItem);
        }
        return serviceItemRepository.save(serviceItem);
    }

    /**
     * 判断是否收费
     *
     * @param serviceItem
     */
    private void judgeCurrentIsCharge(ServiceItem serviceItem) {
        if (serviceItem.getCharge()) {//收费
            serviceItem.setStatus(ServiceItemStatus.PAIED.getStatus());//代付费
        } else {//不收费
            serviceItem.setStatus(ServiceItemStatus.COMPLETED.getStatus());//完成
        }
        //设置下一步的执行开始时间
        ServiceItem nextItem = findNextItem(serviceItem);
        if (nextItem == null) {
            return;
        }
        nextItem.setBeginTime(new Date());
        serviceItemRepository.save(nextItem);
    }

    /**
     * 递归查询下一服务项目
     *
     * @param serviceItem
     * @return
     */
    @SuppressWarnings("all")
    private ServiceItem findNextItem(ServiceItem serviceItem) {
        Integer orderNumber = serviceItem.getOrderNumber();
        ServiceItem nextItem = serviceItemRepository.findByWorkOrderIdAndOrderNumber(serviceItem.getWorkOrder().getId(), ++orderNumber);
        if (nextItem == null) {
            return null;
        }
        if (nextItem.getStatus().equals(ServiceItemStatus.INEXECUTION.getStatus())) {//如果是不执行,则查询下一条
            return findNextItem(nextItem);
        }
        return nextItem;
    }

    /**
     * 判断当前步是否需要审核
     *
     * @param serviceItem
     */
    private Boolean judgeCurrentIsAudit(ServiceItem serviceItem) {
        serviceItem.setEndTime(new Date());//结束时间
        if (serviceItem.getAudit()) {//需要审核
            serviceItem.setStatus(ServiceItemStatus.AUDITED.getStatus());//待审核状态
            WorkOrder workOrder = serviceItem.getWorkOrder();
            workOrder.setSystemStatus(WorkOrderSystemStatus.APPROVED.getStatus());
            workOrderRepository.save(workOrder);
            return true;
        } else {//不需要审核
            //状态更改为待收费还是完成由第四步确认
            return false;
        }
    }

    /**
     * 判断当前步状态是否正常
     *
     * @param serviceItem
     */
    private void judgeCurrentIsNormal(ServiceItem serviceItem) {
        if (!ArrayUtils.contains(ServiceItemUtils.isNormal, serviceItem.getStatus())) {
            throw new BusinessException("当前处理服务项目状态异常");
        }
    }

    /**
     * 判断上一步是否已经做完
     *
     * @param serviceItem
     */
    private void judgeLateIsDone(ServiceItem serviceItem) {
        Integer orderNumber = serviceItem.getOrderNumber();
        if (orderNumber == 1) {//非第一步才有上一步
            return;
        }
        ServiceItem lateItem = serviceItemRepository.findByWorkOrderIdAndOrderNumber(serviceItem.getWorkOrder().getId(), orderNumber - 1);
        if (!ArrayUtils.contains(ServiceItemUtils.isDone, lateItem.getStatus())) {//如果不是完成的状态标志则抛出异常
            throw new BusinessException("上一步未完成");
        }
    }

}
