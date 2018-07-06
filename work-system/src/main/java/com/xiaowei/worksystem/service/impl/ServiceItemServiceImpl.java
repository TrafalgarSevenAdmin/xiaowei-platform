package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.worksystem.entity.ServiceItem;
import com.xiaowei.worksystem.entity.WorkOrder;
import com.xiaowei.worksystem.repository.ServiceItemRepository;
import com.xiaowei.worksystem.repository.WorkOrderRepository;
import com.xiaowei.worksystem.service.IServiceItemService;
import com.xiaowei.worksystem.status.ServiceItemSource;
import com.xiaowei.worksystem.status.ServiceItemStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class ServiceItemServiceImpl extends BaseServiceImpl<ServiceItem> implements IServiceItemService {

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    public ServiceItemServiceImpl(@Qualifier("serviceItemRepository")BaseRepository repository) {
        super(repository);
    }


    /**
     * 工程师添加收费项目
     * @param serviceItems
     * @return
     */
    @Override
    @Transactional
    public List<ServiceItem> saveByEngineer(String workOrderId, List<ServiceItem> serviceItems) {
        WorkOrder workOrder = workOrderRepository.getOne(workOrderId);
        Integer maxOrderNumber = serviceItemRepository.findMaxOrderNumberByWorkOrderId(workOrderId);
        if(maxOrderNumber==null){
            maxOrderNumber = 0;
        }

        for (int i = 0; i < serviceItems.size(); i++) {
            ServiceItem serviceItem = serviceItems.get(i);
            EmptyUtils.assertObject(serviceItem.getCharge(),"是否收费必填");
            EmptyUtils.assertString(serviceItem.getServiceType(),"服务项目类型必填");
            serviceItem.setStatus(ServiceItemStatus.CONFIRMED.getStatus());//待确认状态
            serviceItem.setSource(ServiceItemSource.ENGINEER.getStatus());//工程师来源
            serviceItem.setAudit(true);//需要审核
            serviceItem.setWorkOrder(workOrder);//所属工单
            serviceItem.setOrderNumber(++maxOrderNumber);//顺序
            serviceItem.setCreatedTime(new Date());
            serviceItemRepository.save(serviceItem);
        }

        return serviceItems;
    }
}
