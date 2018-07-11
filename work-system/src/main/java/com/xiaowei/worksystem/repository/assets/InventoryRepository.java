package com.xiaowei.worksystem.repository.assets;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.worksystem.entity.assets.Inventory;

import java.util.List;

public interface InventoryRepository extends BaseRepository<Inventory> {
    /**
     * 获取某个工程师的部分库存
     * @param userId
     * @param ids
     * @return
     */
    List<Inventory> findByWarehouse_User_IdAndIdIn(String userId, List<String> ids);
}
