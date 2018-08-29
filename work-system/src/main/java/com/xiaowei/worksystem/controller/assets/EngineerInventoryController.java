package com.xiaowei.worksystem.controller.assets;

import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.worksystem.dto.InventoryChageDTO;
import com.xiaowei.worksystem.entity.assets.Inventory;
import com.xiaowei.worksystem.service.assets.IInventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


/**
 * 工程师厂库中的备品备件中的库存信息
 */
@Api(tags = "备品备件中的库存信息")
@RestController
@RequestMapping("/api/engineer/inventory")
public class EngineerInventoryController {

    @Autowired
    private IInventoryService inventoryService;

    @ApiOperation("消耗了当前工程师下厂库中的某种配件，不管是好件与坏件")
    @AutoErrorHandler
    @PutMapping("/consume")
    @RequiresPermissions("order:assets:inventory:engineer:consume")
    public Result useProduct(InventoryChageDTO inventoryChageDTO, BindingResult bindingResult) {
        inventoryService.consume(inventoryChageDTO);
        return Result.getSuccess();
    }

    @ApiOperation("获取当前工程师厂库中的所有备品备件")
    @GetMapping("/me")
    @RequiresPermissions("order:assets:inventory:engineer:query")
    public Result list(@ApiParam("每一页的数量") @RequestParam(defaultValue = "10") Integer size,
                       @ApiParam("页数") @RequestParam(defaultValue = "1") Integer page, FieldsView fieldsView) {
        String userId = LoginUserUtils.getLoginUser().getId();
        Query query = new Query();
        query.setPage(page);
        query.setPageSize(size);
        //只查询当前登录的工程师自己的厂库。
        query.addFilter(Filter.eq("warehouse.user.id", userId));
        PageResult pageResult = inventoryService.queryPage(query, Inventory.class);
        pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
        return Result.getSuccess(pageResult);//以分页列表形式返回
    }

}
