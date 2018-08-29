package com.xiaowei.worksystem.service.impl.assets;

import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.worksystem.entity.assets.InvOrderIn;
import com.xiaowei.worksystem.entity.assets.InvOrderInItem;
import com.xiaowei.worksystem.entity.assets.Inventory;
import com.xiaowei.worksystem.entity.assets.Warehouse;
import com.xiaowei.worksystem.repository.assets.InvOrderInRepository;
import com.xiaowei.worksystem.service.assets.IInvOrderInService;
import com.xiaowei.worksystem.service.assets.IInventoryService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvOrderInServiceImpl extends BaseServiceImpl<InvOrderIn> implements IInvOrderInService {

    @Autowired
    private IInventoryService inventoryService;

    public InvOrderInServiceImpl(@Qualifier("invOrderInRepository") InvOrderInRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public void handleInvOrderIn(InvOrderIn invOrderIn) {
        Warehouse outWarehouse = invOrderIn.getOutWarehouse();
        Warehouse inWarehouse = invOrderIn.getInWarehouse();
        for (InvOrderInItem invOrderInItem : invOrderIn.getInvOrderInItems()) {

            //查询入库单中的调出厂库此件的信息
            List<Inventory> inventoriesOnOutWarehouse = inventoryService.query(new Query().addFilter(Filter.eq("warehouse.id", outWarehouse.getId()))
                    .addFilter(Filter.eq("product.id", invOrderInItem.getProduct().getId())));
            if (CollectionUtils.isEmpty(inventoriesOnOutWarehouse)) {
                throw new BusinessException("调出厂库中没有查询到此产品！产品名称：" + invOrderInItem.getProduct().getName());
            }
            Inventory inventoryOnOutWarehouse = inventoriesOnOutWarehouse.get(0);
            if (inventoryOnOutWarehouse.getFineNumber() < invOrderInItem.getFineNumber()) {
                throw new BusinessException("调出厂库此产品(" + invOrderInItem.getProduct().getName() + ")好件数量(" + inventoryOnOutWarehouse.getFineNumber() + ")小于申请调出的数量(" + invOrderInItem.getFineNumber() + ")");
            }
            if (inventoryOnOutWarehouse.getBadNumber() < invOrderInItem.getBadNumber()) {
                throw new BusinessException("调出厂库此产品(" + invOrderInItem.getProduct().getName() + ")坏件数量(" + inventoryOnOutWarehouse.getBadNumber() + ")小于申请调出的数量(" + invOrderInItem.getBadNumber() + ")");
            }
            //修改调出厂库中的此件信息
            inventoryOnOutWarehouse.setFineNumber(inventoryOnOutWarehouse.getFineNumber() - invOrderInItem.getFineNumber());
            inventoryOnOutWarehouse.setBadNumber(inventoryOnOutWarehouse.getBadNumber() - invOrderInItem.getBadNumber());
            inventoryService.save(inventoryOnOutWarehouse);

            //查询入库单中的调入厂库此件的信息
            List<Inventory> inventoriesOnInWarehouse = inventoryService.query(new Query().addFilter(Filter.eq("warehouse.id", inWarehouse.getId()))
                    .addFilter(Filter.eq("product.id", invOrderInItem.getProduct().getId())));
            Inventory inventoryOnInWarehouse;
            //更改调入厂库此件数量
            if (CollectionUtils.isNotEmpty(inventoriesOnInWarehouse)) {
                inventoryOnInWarehouse = inventoriesOnInWarehouse.get(0);
                inventoryOnInWarehouse.setFineNumber(inventoryOnInWarehouse.getFineNumber() + invOrderInItem.getFineNumber());
                inventoryOnInWarehouse.setBadNumber(inventoryOnInWarehouse.getBadNumber() + invOrderInItem.getBadNumber());
            }else{
                inventoryOnInWarehouse = new Inventory();
                inventoryOnInWarehouse.setWarehouse(inWarehouse);
                inventoryOnInWarehouse.setProduct(invOrderInItem.getProduct());
                inventoryOnInWarehouse.setFineNumber(invOrderInItem.getFineNumber());
                inventoryOnInWarehouse.setBadNumber(invOrderInItem.getBadNumber());
                inventoryOnInWarehouse.setSaveNumber(invOrderInItem.getProduct().getSaveNumbe());
            }
            inventoryService.save(inventoryOnInWarehouse);
        }
    }
}