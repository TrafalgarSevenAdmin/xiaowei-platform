package com.xiaowei.accountweb.rest;

import com.xiaowei.account.consts.AccountConst;
import com.xiaowei.account.entity.Tenement;
import com.xiaowei.account.query.TenementQuery;
import com.xiaowei.account.service.ITenementService;
import com.xiaowei.accountweb.dto.TenementDTO;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户管理
 */
@Api(tags = "租户接口")
@RestController
@RequestMapping("/api/tenement")
public class TenementController {

    @Autowired
    private ITenementService tenementService;

    @ApiOperation(value = "添加租户")
    @AutoErrorHandler
    @PostMapping("")
    @RequiresPermissions("account:tenement:add")
    public Result insert(@RequestBody @Validated(V.Insert.class) TenementDTO tenementDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Tenement tenement = BeanCopyUtils.copy(tenementDTO, Tenement.class);
        tenement = tenementService.saveTenement(tenement);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(tenement, fieldsView));
    }

    @ApiOperation(value = "修改租户")
    @AutoErrorHandler
    @PutMapping("/{tenementId}")
    @RequiresPermissions("account:tenement:update")
    public Result update(@PathVariable("tenementId") String tenementId, @RequestBody @Validated(V.Update.class) TenementDTO tenementDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Tenement tenement = BeanCopyUtils.copy(tenementDTO, Tenement.class);
        tenement.setId(tenementId);
        tenement = tenementService.updateTenement(tenement);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(tenement, fieldsView));
    }

    @ApiOperation(value = "启用/禁用租户")
    @AutoErrorHandler
    @PutMapping("/{tenementId}/status")
    @RequiresPermissions("account:tenement:status")
    public Result updateStatus(@PathVariable("tenementId") String tenementId, @RequestBody @Validated(TenementDTO.UpdateStatus.class) TenementDTO tenementDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Tenement tenement = BeanCopyUtils.copy(tenementDTO, Tenement.class);
        tenement.setId(tenementId);
        tenement = tenementService.updateStatus(tenement);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(tenement, fieldsView));
    }

    @ApiOperation(value = "切换admin的租户")
    @PutMapping("/{tenementId}/admin/check")
    @RequiresPermissions("account:tenement:admin:check")
    public Result checkTenementIdForAdmin(@PathVariable("tenementId") String tenementId) throws Exception {
        Tenement tenement = tenementService.findById(tenementId);
        EmptyUtils.assertObject(tenement,"没有查询到需要切换的租户");
        AccountConst.ADMIN_TENENCYID = tenementId;
        return Result.getSuccess();
    }


    @ApiOperation("租户查询接口")
    @GetMapping("")
    @RequiresPermissions("account:tenement:query")
    public Result query(TenementQuery tenementQuery, FieldsView fieldsView) {
        //查询租户设置默认条件
        setDefaultCondition(tenementQuery);

        if (tenementQuery.isNoPage()) {
            List<Tenement> tenements = tenementService.query(tenementQuery, Tenement.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(tenements, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = tenementService.queryPage(tenementQuery, Tenement.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(TenementQuery tenementQuery) {

    }

    @ApiOperation("根据id获取租户")
    @GetMapping("/{tenementId}")
    @RequiresPermissions("account:tenement:get")
    public Result findById(@PathVariable("tenementId") String tenementId, FieldsView fieldsView) {
        Tenement tenement = tenementService.findById(tenementId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(tenement, fieldsView));
    }

    @ApiOperation("删除租户")
    @DeleteMapping("/{tenementId}")
    @RequiresPermissions("account:tenement:delete")
    public Result delete(@PathVariable("tenementId") String tenementId, FieldsView fieldsView) {
        tenementService.delete(tenementId);
        return Result.getSuccess();
    }


}
