package com.xiaowei.worksystem.service.impl.assets;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.assets.Warehouse;
import com.xiaowei.worksystem.service.assets.IWarehouseService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class WarehouseServiceImpl extends BaseServiceImpl<Warehouse> implements IWarehouseService {

    public WarehouseServiceImpl(@Qualifier("warehouseRepository") BaseRepository repository) {
        super(repository);
    }

}