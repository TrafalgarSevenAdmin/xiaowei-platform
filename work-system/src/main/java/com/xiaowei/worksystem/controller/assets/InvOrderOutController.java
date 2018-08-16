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
import com.xiaowei.worksystem.entity.assets.InvOrderOut;
import com.xiaowei.worksystem.service.assets.IInvOrderOutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "出库单")
@Slf4j
@RestController
@RequestMapping("/api/assets/invOrderOut")
public class InvOrderOutController {

    @Autowired
    private IInvOrderOutService invOrderOutService;

    @ApiOperation(value = "添加")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) InvOrderOut invOrderOutDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        InvOrderOut invOrderOut = BeanCopyUtils.copy(invOrderOutDto, InvOrderOut.class);
        invOrderOut = invOrderOutService.save(invOrderOut);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(invOrderOut, fieldsView));
    }

    @ApiOperation(value = "修改")
    @AutoErrorHandler
    @PutMapping("/{invOrderOutId}")
    public Result update(@RequestBody @Validated(V.Insert.class) InvOrderOut invOrderOutDto, BindingResult bindingResult,
                         @PathVariable("invOrderOutId") String invOrderOutId, FieldsView fieldsView) throws Exception {
        InvOrderOut invOrderOut = BeanCopyUtils.copy(invOrderOutDto, InvOrderOut.class);
        invOrderOut = invOrderOutService.save(invOrderOut);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(invOrderOut, fieldsView));
    }


    @ApiOperation("根据id获取")
    @GetMapping("/{invOrderOutId}")
    public Result findById(@PathVariable("invOrderOutId") String invOrderOutId, FieldsView fieldsView) {
        InvOrderOut invOrderOut = invOrderOutService.findById(invOrderOutId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(invOrderOut, fieldsView));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{invOrderOutId}")
    public Result delete(@PathVariable("invOrderOutId") String invOrderOutId, FieldsView fieldsView) {
        invOrderOutService.delete(invOrderOutId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("查询接口")
    @GetMapping("")
    public Result query(Query query, FieldsView fieldsView) {
        if (query.isNoPage()) {
            List<InvOrderOut> invOrderOuts = invOrderOutService.query(query, InvOrderOut.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(invOrderOuts, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = invOrderOutService.queryPage(query, InvOrderOut.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}
