package com.xiaowei.worksystem.service.assets;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.dto.InventoryChageDTO;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.entity.assets.Inventory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface IInventoryService extends IBaseService<Inventory> {

    void consume(InventoryChageDTO inventoryChageDTO);
}
