package com.xiaowei.worksystem.controller;

import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.dto.WorkpieceDTO;
import com.xiaowei.worksystem.entity.Workpiece;
import com.xiaowei.worksystem.service.IWorkpieceService;
import com.xiaowei.worksystem.status.CommonStatus;
import com.xiaowei.worksystem.status.WorkpieceStoreType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工件接口
 */
@Api(tags = "工件接口")
@RestController
@RequestMapping("/api/workpiece")
public class WorkpieceController {

    /**
     * 工件服务
     */
    @Autowired
    private IWorkpieceService workpieceService;

    /**
     * 后台添加工件
     * 属于后台使用
     * @param workpieceDTO
     * @param bindingResult
     * @param fieldsView
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "后台添加工件")
    @AutoErrorHandler
    @PostMapping("")
    // TODO: 2018/7/5 0005 判断是否是后台操作权限
    public Result insert(@RequestBody @Validated(V.Insert.class) WorkpieceDTO workpieceDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Workpiece workpiece = BeanCopyUtils.copy(workpieceDTO, Workpiece.class);
        workpiece.setStoreType(WorkpieceStoreType.CORE.getStatus());
        workpiece = workpieceService.save(workpiece);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(workpiece, fieldsView));
    }

    /**
     * 后台添加工件
     * 属于后台使用
     * @param workpieceDTO
     * @param bindingResult
     * @param fieldsView
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "工程师添加坏件",notes = "注意，工程师不能直接添加好件。若工程师厂库中存在此条坏件的信息，就将坏件的数量相加后保存")
    @AutoErrorHandler
    @PostMapping("/bad")
    // TODO: 2018/7/5 0005 判断是否是工程师权限
    public Result insertbad(@RequestBody @Validated(WorkpieceDTO.EngineerAddBadWorkpiece.class) WorkpieceDTO workpieceDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Workpiece workpiece = BeanCopyUtils.copy(workpieceDTO, Workpiece.class);
        workpiece.setStoreType(WorkpieceStoreType.USER.getStatus());
        workpiece.setUserId(LoginUserUtils.getLoginUser().getId());
        workpiece.setFineTotal(0);
        workpiece = workpieceService.save(workpiece);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(workpiece, fieldsView));
    }

    @ApiOperation(value = "修改工件")
    @AutoErrorHandler
    @PutMapping("/")
    public Result update(@RequestBody @Validated(V.Insert.class) WorkpieceDTO workpieceDTO, BindingResult bindingResult,
                         @PathVariable("equipmentId") String equipmentId, FieldsView fieldsView) throws Exception {
        Workpiece workpiece = BeanCopyUtils.copy(workpieceDTO, Workpiece.class);
        workpiece = workpieceService.update(workpiece);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(workpiece, fieldsView));
    }


    @ApiOperation("根据id获取工件/仓库详情")
    @GetMapping("/{workpieceId}")
    public Result findById(@PathVariable("workpieceId") String workpieceId, FieldsView fieldsView) {
        Workpiece workpiece = workpieceService.findById(workpieceId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(workpiece, fieldsView));
    }

    @ApiOperation("删除工件")
    @DeleteMapping("/{workpieceId}")
    public Result delete(@PathVariable("workpieceId") String workpieceId, FieldsView fieldsView) {
        workpieceService.fakeDelete(workpieceId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("工件查询接口")
    @GetMapping("")
    public Result query(Query query, FieldsView fieldsView) {
        //过滤删除的数据
        query.addFilter(new Filter("status", Filter.Operator.neq, CommonStatus.DELETE.getStatus()));
        if (query.isNoPage()) {
            List<Workpiece> workpieces = workpieceService.query(query, Workpiece.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(workpieces, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = workpieceService.queryPage(query, Workpiece.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}
