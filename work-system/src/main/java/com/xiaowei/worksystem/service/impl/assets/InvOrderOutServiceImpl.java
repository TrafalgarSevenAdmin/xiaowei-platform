package com.xiaowei.worksystem.service.impl.assets;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.worksystem.entity.assets.*;
import com.xiaowei.worksystem.service.IInventoryService;
import com.xiaowei.worksystem.service.assets.IInvOrderOutService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Service
public class InvOrderOutServiceImpl extends BaseServiceImpl<InvOrderOut> implements IInvOrderOutService {

    @Autowired
    private IInventoryService inventoryService;
    
    public InvOrderOutServiceImpl(@Qualifier("invOrderOutRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    public void handleInvOrderOut(InvOrderOut invOrderOut) {
        Warehouse outWarehouse = invOrderOut.getOutWarehouse();
        Warehouse inWarehouse = invOrderOut.getInWarehouse();
        for (InvOrderOutItem invOrderOutItem : invOrderOut.getInvOrderOutItems()) {
            //查询入库单中的调出厂库此件的信息
            List<Inventory> inventoriesOnOutWarehouse = inventoryService.query(new Query().addFilter(Filter.eq("warehouse.id", outWarehouse.getId()))
                    .addFilter(Filter.eq("product.id", invOrderOutItem.getProduct().getId())));
            if (CollectionUtils.isEmpty(inventoriesOnOutWarehouse)) {
                throw new BusinessException("调出厂库中没有查询到此产品！产品名称：" + invOrderOutItem.getProduct().getName());
            }
            Inventory inventoryOnOutWarehouse = inventoriesOnOutWarehouse.get(0);
            if (inventoryOnOutWarehouse.getFineNumber() < invOrderOutItem.getFineNumber()) {
                throw new BusinessException("调出厂库此产品(" + invOrderOutItem.getProduct().getName() + ")好件数量(" + inventoryOnOutWarehouse.getFineNumber() + ")小于申请调出的数量(" + invOrderOutItem.getFineNumber() + ")");
            }
            if (inventoryOnOutWarehouse.getBadNumber() < invOrderOutItem.getBadNumber()) {
                throw new BusinessException("调出厂库此产品(" + invOrderOutItem.getProduct().getName() + ")坏件数量(" + inventoryOnOutWarehouse.getBadNumber() + ")小于申请调出的数量(" + invOrderOutItem.getBadNumber() + ")");
            }
            //修改调出厂库中的此件信息
            inventoryOnOutWarehouse.setFineNumber(inventoryOnOutWarehouse.getFineNumber() - invOrderOutItem.getFineNumber());
            inventoryOnOutWarehouse.setBadNumber(inventoryOnOutWarehouse.getBadNumber() - invOrderOutItem.getBadNumber());
            inventoryService.save(inventoryOnOutWarehouse);

            //查询入库单中的调入厂库此件的信息
            List<Inventory> inventoriesOnInWarehouse = inventoryService.query(new Query().addFilter(Filter.eq("warehouse.id", inWarehouse.getId()))
                    .addFilter(Filter.eq("product.id", invOrderOutItem.getProduct().getId())));
            Inventory inventoryOnInWarehouse;
            //更改调入厂库此件数量
            if (CollectionUtils.isNotEmpty(inventoriesOnInWarehouse)) {
                inventoryOnInWarehouse = inventoriesOnInWarehouse.get(0);
                inventoryOnInWarehouse.setFineNumber(inventoryOnInWarehouse.getFineNumber() + invOrderOutItem.getFineNumber());
                inventoryOnInWarehouse.setBadNumber(inventoryOnInWarehouse.getBadNumber() + invOrderOutItem.getBadNumber());
            }else{
                inventoryOnInWarehouse = new Inventory();
                inventoryOnInWarehouse.setWarehouse(inWarehouse);
                inventoryOnInWarehouse.setProduct(invOrderOutItem.getProduct());
                inventoryOnInWarehouse.setFineNumber(invOrderOutItem.getFineNumber());
                inventoryOnInWarehouse.setBadNumber(invOrderOutItem.getBadNumber());
                inventoryOnInWarehouse.setSaveNumber(invOrderOutItem.getProduct().getSaveNumbe());
            }
            inventoryService.save(inventoryOnInWarehouse);
        }
    }
}