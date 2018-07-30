package com.xiaowei.worksystem.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.ServiceItem;

import java.util.List;


public interface IServiceItemService extends IBaseService<ServiceItem> {

    List<ServiceItem> saveByEngineer(String workOrderId, List<ServiceItem> serviceItems);

    ServiceItem executeServiceItem(String serviceItemId, String qualityFileStore);

    ServiceItem qualityServiceItem(String serviceItemId, Boolean audit);
}
