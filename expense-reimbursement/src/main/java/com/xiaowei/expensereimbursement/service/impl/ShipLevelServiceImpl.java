package com.xiaowei.expensereimbursement.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.expensereimbursement.entity.ShipLevel;
import com.xiaowei.expensereimbursement.repository.ShipLevelRepository;
import com.xiaowei.expensereimbursement.service.IShipLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class ShipLevelServiceImpl extends BaseServiceImpl<ShipLevel> implements IShipLevelService {

    @Autowired
    private ShipLevelRepository shipLevelRepository;


    public ShipLevelServiceImpl(@Qualifier("shipLevelRepository") BaseRepository repository) {
        super(repository);
    }

}
