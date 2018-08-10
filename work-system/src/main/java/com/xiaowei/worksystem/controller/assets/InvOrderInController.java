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
import com.xiaowei.worksystem.entity.assets.InvOrderIn;
import com.xiaowei.worksystem.service.assets.IInvOrderInService;
import com.xiaowei.worksystem.status.CommonStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "入库单")
@RestController
@RequestMapping("/api/assets/invOrderIn")
public class InvOrderInController {

    @Autowired
    private IInvOrderInService invOrderInService;

    @ApiOperation(value = "添加")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) InvOrderIn invOrderInDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        InvOrderIn invOrderIn = BeanCopyUtils.copy(invOrderInDto, InvOrderIn.class);
        invOrderIn = invOrderInService.save(invOrderIn);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(invOrderIn, fieldsView));
    }

    @ApiOperation(value = "修改")
    @AutoErrorHandler
    @PutMapping("/{invOrderInId}")
    public Result update(@RequestBody @Validated(V.Insert.class) InvOrderIn invOrderInDto, BindingResult bindingResult,
                         @PathVariable("invOrderInId") String invOrderInId, FieldsView fieldsView) throws Exception {
        InvOrderIn invOrderIn = BeanCopyUtils.copy(invOrderInDto, InvOrderIn.class);
        invOrderIn = invOrderInService.save(invOrderIn);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(invOrderIn, fieldsView));
    }


    @ApiOperation("根据id获取")
    @GetMapping("/{invOrderInId}")
    public Result findById(@PathVariable("invOrderInId") String invOrderInId, FieldsView fieldsView) {
        InvOrderIn invOrderIn = invOrderInService.findById(invOrderInId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(invOrderIn, fieldsView));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{invOrderInId}")
    public Result delete(@PathVariable("invOrderInId") String invOrderInId, FieldsView fieldsView) {
        invOrderInService.delete(invOrderInId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("查询接口")
    @GetMapping("")
    public Result query(Query query, FieldsView fieldsView) {
        query.addFilter(new Filter("status", Filter.Operator.neq, CommonStatus.DELETE.getStatus()));
        if (query.isNoPage()) {
            List<InvOrderIn> invOrderIns = invOrderInService.query(query, InvOrderIn.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(invOrderIns, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = invOrderInService.queryPage(query, InvOrderIn.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}
