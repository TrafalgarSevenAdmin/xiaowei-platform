package com.xiaowei.worksystem.controller.assets;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.assets.Warehouse;
import com.xiaowei.worksystem.service.assets.IWarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "仓库档案信息")
@Log4j2
@RestController
@RequestMapping("/api/assets/warehouse")
public class WarehouseController {

    @Autowired
    private IWarehouseService warehouseService;

    @ApiOperation(value = "添加")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) Warehouse warehouseDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Warehouse warehouse = BeanCopyUtils.copy(warehouseDto, Warehouse.class);
        warehouse = warehouseService.save(warehouse);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(warehouse, fieldsView));
    }

    @ApiOperation(value = "修改")
    @AutoErrorHandler
    @PutMapping("/{warehouseId}")
    public Result update(@RequestBody @Validated(V.Insert.class) Warehouse warehouseDto, BindingResult bindingResult,
                         @PathVariable("warehouseId") String warehouseId, FieldsView fieldsView) throws Exception {
        Warehouse warehouse = BeanCopyUtils.copy(warehouseDto, Warehouse.class);
        warehouse = warehouseService.save(warehouse);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(warehouse, fieldsView));
    }


    @ApiOperation("根据id获取")
    @GetMapping("/{warehouseId}")
    public Result findById(@PathVariable("warehouseId") String warehouseId, FieldsView fieldsView) {
        Warehouse warehouse = warehouseService.findById(warehouseId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(warehouse, fieldsView));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{warehouseId}")
    public Result delete(@PathVariable("warehouseId") String warehouseId, FieldsView fieldsView) {
        warehouseService.delete(warehouseId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("查询接口")
    @GetMapping("")
    public Result query(Query query, FieldsView fieldsView) {
        if (query.isNoPage()) {
            List<Warehouse> warehouses = warehouseService.query(query, Warehouse.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(warehouses, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = warehouseService.queryPage(query, Warehouse.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}
