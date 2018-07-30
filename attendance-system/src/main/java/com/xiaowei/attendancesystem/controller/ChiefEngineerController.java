package com.xiaowei.attendancesystem.controller;

import com.xiaowei.attendancesystem.dto.ChiefEngineerDTO;
import com.xiaowei.attendancesystem.entity.ChiefEngineer;
import com.xiaowei.attendancesystem.query.ChiefEngineerQuery;
import com.xiaowei.attendancesystem.service.IChiefEngineerService;
import com.xiaowei.commonjts.utils.GeometryUtil;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "办公点接口")
@RestController
@RequestMapping("/api/chief")
public class ChiefEngineerController {

    @Autowired
    private IChiefEngineerService chiefEngineerService;

    @ApiOperation(value = "添加办公点")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) ChiefEngineerDTO chiefEngineerDTO,
                         BindingResult bindingResult,
                         FieldsView fieldsView) throws Exception {
        ChiefEngineer chiefEngineer = BeanCopyUtils.copy(chiefEngineerDTO, ChiefEngineer.class);
        chiefEngineer.setShape(GeometryUtil.transWKT(chiefEngineerDTO.getWkt()));//设置图斑数据
        chiefEngineer = chiefEngineerService.saveChiefEngineer(chiefEngineer);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(chiefEngineer, fieldsView));
    }

    @ApiOperation(value = "修改办公点")
    @AutoErrorHandler
    @PutMapping("/{chiefEngineerId}")
    public Result update(@PathVariable("chiefEngineerId") String chiefEngineerId,
                         @RequestBody @Validated(V.Update.class) ChiefEngineerDTO chiefEngineerDTO,
                         BindingResult bindingResult,
                         FieldsView fieldsView) throws Exception {
        ChiefEngineer chiefEngineer = BeanCopyUtils.copy(chiefEngineerDTO, ChiefEngineer.class);
        chiefEngineer.setId(chiefEngineerId);
        chiefEngineer.setShape(GeometryUtil.transWKT(chiefEngineerDTO.getWkt()));//设置图斑数据
        chiefEngineer = chiefEngineerService.updateChiefEngineer(chiefEngineer);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(chiefEngineer, fieldsView));
    }

    @ApiOperation(value = "启用/禁用办公点")
    @AutoErrorHandler
    @PutMapping("/{chiefEngineerId}/status")
    public Result updateStatus(@PathVariable("chiefEngineerId") String chiefEngineerId,
                               @RequestBody @Validated(ChiefEngineerDTO.UpdateStatus.class) ChiefEngineerDTO chiefEngineerDTO,
                               BindingResult bindingResult,
                               FieldsView fieldsView) throws Exception {
        ChiefEngineer chiefEngineer = BeanCopyUtils.copy(chiefEngineerDTO, ChiefEngineer.class);
        chiefEngineer.setId(chiefEngineerId);
        chiefEngineer = chiefEngineerService.updateStatus(chiefEngineer);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(chiefEngineer, fieldsView));
    }

    @ApiOperation("删除办公点")
    @DeleteMapping("/{chiefEngineerId}")
    public Result delete(@PathVariable("chiefEngineerId") String chiefEngineerId, FieldsView fieldsView) {
        chiefEngineerService.fakeDeleteChiefEngineer(chiefEngineerId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("办公点查询接口")
    @GetMapping("")
    public Result query(ChiefEngineerQuery chiefEngineerQuery, FieldsView fieldsView) {
        //查询办公点设置默认条件
        setDefaultCondition(chiefEngineerQuery);

        if (chiefEngineerQuery.isNoPage()) {
            List<ChiefEngineer> chiefEngineers = chiefEngineerService.query(chiefEngineerQuery, ChiefEngineer.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(chiefEngineers, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = chiefEngineerService.queryPage(chiefEngineerQuery, ChiefEngineer.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(ChiefEngineerQuery chiefEngineerQuery) {

    }

    @ApiOperation("根据id获取办公点")
    @GetMapping("/{chiefEngineerId}")
    public Result findById(@PathVariable("chiefEngineerId") String chiefEngineerId, FieldsView fieldsView) {
        ChiefEngineer chiefEngineer = chiefEngineerService.findById(chiefEngineerId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(chiefEngineer, fieldsView));
    }

    @ApiOperation("获取当前登录用户最近的办公点")
    @GetMapping("/nearest")
    public Result findNearest(@RequestParam String wkt, FieldsView fieldsView) throws Exception {
        ChiefEngineer chiefEngineer = chiefEngineerService.findNearest(GeometryUtil.transWKT(wkt));
        return Result.getSuccess(ObjectToMapUtils.objectToMap(chiefEngineer, fieldsView));
    }



}
