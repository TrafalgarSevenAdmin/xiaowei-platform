package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.ServiceItem;
import com.xiaowei.worksystem.repository.ServiceItemRepository;
import com.xiaowei.worksystem.service.IServiceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class ServiceItemServiceImpl extends BaseServiceImpl<ServiceItem> implements IServiceItemService {

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    public ServiceItemServiceImpl(@Qualifier("serviceItemRepository")BaseRepository repository) {
        super(repository);
    }


}
