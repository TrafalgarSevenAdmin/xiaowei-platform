package com.xiaowei.worksystem.service.impl;

import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.worksystem.dto.InventoryChageDTO;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.entity.EquipmentModified;
import com.xiaowei.worksystem.entity.WorkOrder;
import com.xiaowei.worksystem.entity.assets.Inventory;
import com.xiaowei.worksystem.repository.EquipmentModifiedRepository;
import com.xiaowei.worksystem.repository.assets.InventoryRepository;
import com.xiaowei.worksystem.service.IEquipmentModifiedService;
import com.xiaowei.worksystem.service.IEquipmentService;
import com.xiaowei.worksystem.service.IInventoryService;
import com.xiaowei.worksystem.service.IWorkOrderService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 库存服务
 */
@Service
public class InventoryServiceImpl extends BaseServiceImpl<Inventory> implements IInventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public InventoryServiceImpl(@Qualifier("inventoryRepository")BaseRepository repository) {
        super(repository);
    }


    @Override
    @Transactional
    public void consume(InventoryChageDTO inventoryChageDTO) {
        String userId = LoginUserUtils.getLoginUser().getId();
        //如果没有修改的库存就不更新
        if (CollectionUtils.isEmpty(inventoryChageDTO.getChages())) {
            return;
        }
        //获取要更改的所有配件的库存信息
        List<String> inventoryIds = inventoryChageDTO.getChages().stream().map(InventoryChageDTO.Chage::getId).distinct().collect(Collectors.toList());
        List<Inventory> inventories = inventoryRepository.findByWarehouse_User_IdAndIdIn(userId, inventoryIds);
        if (CollectionUtils.isEmpty(inventories) || inventories.size() != inventoryIds.size()) {
            throw new BusinessException("未找到需要更新的库存信息！");
        }
        Map<String, InventoryChageDTO.Chage> chages = inventoryChageDTO.getChages().stream().collect(Collectors.toMap(t -> t.getId(), t -> t));
        for (Inventory inventory : inventories) {
            String id = inventory.getId();
            InventoryChageDTO.Chage chage = chages.get(id);
            //计算现在的库存
            int badNumber = inventory.getBadNumber() + chage.getBadChage();
            int fineNumber = inventory.getFineNumber() - chage.getFineChage();
            if (badNumber < 0 && fineNumber < 0) {
                throw new BusinessException("数据错误！" + inventory.getProduct().getName() + "的好/坏件库存数量不能为负数");
            }
            inventory.setBadNumber(badNumber);
            inventory.setFineNumber(fineNumber);
            //保存库存
            inventoryRepository.save(inventory);
        }

    }
}
