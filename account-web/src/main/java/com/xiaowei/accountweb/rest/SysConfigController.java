package com.xiaowei.accountweb.rest;

import com.xiaowei.account.entity.SysConfig;
import com.xiaowei.account.service.ISysConfigService;
import com.xiaowei.account.utils.ConfigUtils;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "系统配置")
@Log4j2
@RestController
@RequestMapping("/api/sysConfig")
public class SysConfigController {

    @Autowired
    private ISysConfigService sysConfigService;

    @ApiOperation(value = "添加")
    @AutoErrorHandler
    @RequiresPermissions("account:config:add")
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) SysConfig sysConfigDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        SysConfig sysConfig = BeanCopyUtils.copy(sysConfigDto, SysConfig.class);
        sysConfig = sysConfigService.save(sysConfig);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(sysConfig, fieldsView));
    }

    @ApiOperation(value = "修改")
    @AutoErrorHandler
    @RequiresPermissions("account:config:update")
    @PutMapping("/{sysConfigId}")
    public Result update(@RequestBody @Validated(V.Insert.class) SysConfig sysConfigDto, BindingResult bindingResult,
                         @PathVariable("sysConfigId") String sysConfigId, FieldsView fieldsView) throws Exception {
        SysConfig sysConfig = BeanCopyUtils.copy(sysConfigDto, SysConfig.class);
        sysConfig = sysConfigService.save(sysConfig);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(sysConfig, fieldsView));
    }


    @ApiOperation("根据id获取")
    @RequiresPermissions("account:config:get")
    @GetMapping("/{sysConfigId}")
    public Result findById(@PathVariable("sysConfigId") String sysConfigId, FieldsView fieldsView) {
        SysConfig sysConfig = sysConfigService.findById(sysConfigId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(sysConfig, fieldsView));
    }

    @ApiOperation("删除")
    @RequiresPermissions("account:config:delete")
    @DeleteMapping("/{sysConfigId}")
    public Result delete(@PathVariable("sysConfigId") String sysConfigId, FieldsView fieldsView) {
        sysConfigService.delete(sysConfigId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("获取配置")
    @GetMapping("/config")
    public Result getConfig(String code) {
        return Result.getSuccess(ConfigUtils.getConfig(code));
    }

    @ApiOperation("查询接口")
    @GetMapping("")
    @RequiresPermissions("account:config:query")
    public Result query(Query query, FieldsView fieldsView) {
        if (query.isNoPage()) {
            List<SysConfig> sysConfigs = sysConfigService.query(query, SysConfig.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(sysConfigs, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = sysConfigService.queryPage(query, SysConfig.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}
