package com.xiaowei.worksystem.controller.assets;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.assets.ProClass;
import com.xiaowei.worksystem.service.assets.IProClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "产品分类")
@Slf4j
@RestController
@RequestMapping("/api/assets/proClass")
public class ProClassController {

    @Autowired
    private IProClassService proClassService;

    @ApiOperation(value = "添加")
    @AutoErrorHandler
    @PostMapping("")
    @RequiresPermissions("order:assets:proClass:add")
    public Result insert(@RequestBody @Validated(V.Insert.class) ProClass proClassDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        ProClass proClass = BeanCopyUtils.copy(proClassDto, ProClass.class);
        proClass = proClassService.save(proClass);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(proClass, fieldsView));
    }

    @ApiOperation(value = "修改")
    @AutoErrorHandler
    @PutMapping("/{proClassId}")
    @RequiresPermissions("order:assets:proClass:update")
    public Result update(@RequestBody @Validated(V.Insert.class) ProClass proClassDto, BindingResult bindingResult,
                         @PathVariable("proClassId") String proClassId, FieldsView fieldsView) throws Exception {
        ProClass proClass = BeanCopyUtils.copy(proClassDto, ProClass.class);
        proClass = proClassService.save(proClass);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(proClass, fieldsView));
    }


    @ApiOperation("根据id获取")
    @GetMapping("/{proClassId}")
    @RequiresPermissions("order:assets:proClass:get")
    public Result findById(@PathVariable("proClassId") String proClassId, FieldsView fieldsView) {
        ProClass proClass = proClassService.findById(proClassId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(proClass, fieldsView));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{proClassId}")
    @RequiresPermissions("order:assets:proClass:delete")
    public Result delete(@PathVariable("proClassId") String proClassId, FieldsView fieldsView) {
        proClassService.delete(proClassId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("查询接口")
    @GetMapping("")
    @RequiresPermissions("order:assets:proClass:query")
    public Result query(Query query, FieldsView fieldsView) {
        if (query.isNoPage()) {
            List<ProClass> proClasss = proClassService.query(query, ProClass.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(proClasss, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = proClassService.queryPage(query, ProClass.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}
