package com.xiaowei.expensereimbursementweb.controller;

import com.xiaowei.commonlog4j.annotation.ContentParam;
import com.xiaowei.commonlog4j.annotation.HandleLog;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.expensereimbursement.entity.ShipLevel;
import com.xiaowei.expensereimbursement.service.IShipLevelService;
import com.xiaowei.expensereimbursementweb.dto.ShipLevelDTO;
import com.xiaowei.expensereimbursementweb.query.ShipLevelQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 舱位级别管理
 */
@Api(tags = "舱位级别接口")
@RestController
@RequestMapping("/api/shipLevel")
public class ShipLevelController {

    @Autowired
    private IShipLevelService shipLevelService;

    @ApiOperation(value = "添加舱位级别")
    @AutoErrorHandler
    @PostMapping("")
    @RequiresPermissions("expense:shipLevel:add")
    @HandleLog(type = "添加舱位级别", contentParams = {@ContentParam(useParamField = true, field = "shipLevelDTO", value = "舱位级别信息")})
    public Result insert(@RequestBody @Validated(V.Insert.class) ShipLevelDTO shipLevelDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        ShipLevel shipLevel = BeanCopyUtils.copy(shipLevelDTO, ShipLevel.class);
        shipLevel = shipLevelService.save(shipLevel);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(shipLevel, fieldsView));
    }

    @ApiOperation(value = "修改舱位级别")
    @AutoErrorHandler
    @PutMapping("/{shipLevelId}")
    @RequiresPermissions("expense:shipLevel:update")
    @HandleLog(type = "修改舱位级别", contentParams = {@ContentParam(useParamField = true, field = "shipLevelDTO", value = "舱位级别信息"),
            @ContentParam(useParamField = false, field = "shipLevelId", value = "舱位级别id")})
    public Result update(@PathVariable("shipLevelId") String shipLevelId,@RequestBody @Validated(V.Update.class) ShipLevelDTO shipLevelDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        ShipLevel shipLevel = BeanCopyUtils.copy(shipLevelDTO, ShipLevel.class);
        shipLevel.setId(shipLevelId);
        shipLevel = shipLevelService.update(shipLevel);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(shipLevel, fieldsView));
    }

    @ApiOperation(value = "删除舱位级别")
    @DeleteMapping("/{shipLevelId}")
    @RequiresPermissions("expense:shipLevel:delete")
    @HandleLog(type = "删除舱位级别", contentParams = {@ContentParam(useParamField = false, field = "shipLevelId", value = "舱位级别id")})
    public Result delete(@PathVariable("shipLevelId") String shipLevelId) throws Exception {
        shipLevelService.delete(shipLevelId);
        return Result.getSuccess();
    }

    @ApiOperation("舱位级别查询接口")
    @GetMapping("")
    @RequiresPermissions("expense:shipLevel:query")
    public Result query(ShipLevelQuery shipLevelQuery, FieldsView fieldsView) {
        //查询舱位级别设置默认条件
        setDefaultCondition(shipLevelQuery);

        if (shipLevelQuery.isNoPage()) {
            List<ShipLevel> shipLevels = shipLevelService.query(shipLevelQuery, ShipLevel.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(shipLevels, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = shipLevelService.queryPage(shipLevelQuery, ShipLevel.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(ShipLevelQuery shipLevelQuery) {

    }

    @ApiOperation("根据id获取舱位级别")
    @GetMapping("/{shipLevelId}")
    @RequiresPermissions("expense:shipLevel:get")
    public Result findById(@PathVariable("shipLevelId") String shipLevelId, FieldsView fieldsView) {
        ShipLevel shipLevel = shipLevelService.findById(shipLevelId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(shipLevel, fieldsView));
    }

}
