package com.xiaowei.worksystem.controller.assets;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.assets.Inventory;
import com.xiaowei.worksystem.service.assets.IInventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "库存信息")
@Log4j2
@RestController
@RequestMapping("/api/assets/inventory")
public class InventoryController {

    @Autowired
    private IInventoryService inventoryService;

    @ApiOperation(value = "添加")
    @AutoErrorHandler
    @PostMapping("")
    @RequiresPermissions("order:assets:inventory:add")
    public Result insert(@RequestBody @Validated(V.Insert.class) Inventory inventoryDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Inventory inventory = BeanCopyUtils.copy(inventoryDto, Inventory.class);
        inventory = inventoryService.save(inventory);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(inventory, fieldsView));
    }

    @ApiOperation(value = "修改")
    @AutoErrorHandler
    @PutMapping("/{inventoryId}")
    @RequiresPermissions("order:assets:inventory:update")
    public Result update(@RequestBody @Validated(V.Insert.class) Inventory inventoryDto, BindingResult bindingResult,
                         @PathVariable("inventoryId") String inventoryId, FieldsView fieldsView) throws Exception {
        Inventory inventory = BeanCopyUtils.copy(inventoryDto, Inventory.class);
        inventory = inventoryService.save(inventory);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(inventory, fieldsView));
    }


    @ApiOperation("根据id获取")
    @GetMapping("/{inventoryId}")
    @RequiresPermissions("order:assets:inventory:get")
    public Result findById(@PathVariable("inventoryId") String inventoryId, FieldsView fieldsView) {
        Inventory inventory = inventoryService.findById(inventoryId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(inventory, fieldsView));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{inventoryId}")
    @RequiresPermissions("order:assets:inventory:delete")
    public Result delete(@PathVariable("inventoryId") String inventoryId, FieldsView fieldsView) {
        inventoryService.delete(inventoryId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("查询接口")
    @GetMapping("")
    @RequiresPermissions("order:assets:inventory:query")
    public Result query(Query query, FieldsView fieldsView) {
        if (query.isNoPage()) {
            List<Inventory> inventorys = inventoryService.query(query, Inventory.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(inventorys, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = inventoryService.queryPage(query, Inventory.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}
