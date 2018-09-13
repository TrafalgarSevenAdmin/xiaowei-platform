package com.xiaowei.expensereimbursementweb.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaowei.account.consts.SuperUser;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.service.IDepartmentService;
import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.accountcommon.LoginUserBean;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.commonlog4j.annotation.ContentParam;
import com.xiaowei.commonlog4j.annotation.HandleLog;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.FastJsonUtils;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.expensereimbursement.entity.AuditConfiguration;
import com.xiaowei.expensereimbursement.service.IAuditConfigurationService;
import com.xiaowei.expensereimbursementweb.dto.AllAuditConfigurationDTO;
import com.xiaowei.expensereimbursementweb.dto.AuditConfigurationDTO;
import com.xiaowei.expensereimbursementweb.query.AuditConfigurationQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 审核配置管理
 */
@Api(tags = "审核配置接口")
@RestController
@RequestMapping("/api/auditConfiguration")
public class AuditConfigurationController {

    @Autowired
    private IAuditConfigurationService auditConfigurationService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation(value = "添加审核配置")
    @AutoErrorHandler
    @PostMapping("")
    @RequiresPermissions("expense:auditConfiguration:add")
    @HandleLog(type = "添加审核配置", contentParams = {@ContentParam(useParamField = true, field = "auditConfigurationDTO", value = "审核配置信息")})
    public Result insert(@RequestBody @Validated(V.Insert.class) AuditConfigurationDTO auditConfigurationDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        AuditConfiguration auditConfiguration = BeanCopyUtils.copy(auditConfigurationDTO, AuditConfiguration.class);
        auditConfiguration = auditConfigurationService.saveAuditConfiguration(auditConfiguration);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(auditConfiguration, fieldsView));
    }

    @ApiOperation(value = "批量添加审核配置")
    @AutoErrorHandler
    @PostMapping("/all")
    @RequiresPermissions("expense:auditConfiguration:add")
    @HandleLog(type = "批量添加审核配置", contentParams = {@ContentParam(useParamField = true, field = "auditConfigurationDTO", value = "审核配置信息")})
    public Result insertAll(@RequestBody @Validated(V.Insert.class) AllAuditConfigurationDTO allAuditConfigurationDTO, BindingResult bindingResult) throws Exception {
        AuditConfiguration auditConfiguration = new AuditConfiguration();
        auditConfiguration.setTypeStatus(allAuditConfigurationDTO.getTypeStatus());
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(auditConfiguration));
        List<JSONObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(jsonObject);
        jsonObjects = FastJsonUtils.transArrToArrMap(allAuditConfigurationDTO.getUserIds(),"userId",jsonObjects);
        jsonObjects = FastJsonUtils.transArrToArrMap(allAuditConfigurationDTO.getDepartmentIds(),"departmentId",jsonObjects);
        List<AuditConfiguration> auditConfigurations = FastJsonUtils.listJsonObjectToListObject(jsonObjects, AuditConfiguration.class);
        auditConfigurationService.saveAllAuditConfiguration(auditConfigurations);
        return Result.getSuccess();
    }

    @ApiOperation(value = "删除审核配置")
    @DeleteMapping("/{auditConfigurationId}")
    @RequiresPermissions("expense:auditConfiguration:delete")
    @HandleLog(type = "删除审核配置", contentParams = {@ContentParam(useParamField = false, field = "auditConfigurationId", value = "审核配置id")})
    public Result delete(@PathVariable("auditConfigurationId") String auditConfigurationId) throws Exception {
        auditConfigurationService.deleteAuditConfiguration(auditConfigurationId);
        return Result.getSuccess();
    }

    @ApiOperation("审核配置查询接口")
    @GetMapping("")
    @RequiresPermissions("expense:auditConfiguration:query")
    public Result query(AuditConfigurationQuery auditConfigurationQuery, FieldsView fieldsView) {
        //查询审核配置设置默认条件
        setDefaultCondition(auditConfigurationQuery);

        if (auditConfigurationQuery.isNoPage()) {
            List<AuditConfiguration> auditConfigurations = auditConfigurationService.query(auditConfigurationQuery, AuditConfiguration.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(auditConfigurations, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = auditConfigurationService.queryPage(auditConfigurationQuery, AuditConfiguration.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(AuditConfigurationQuery auditConfigurationQuery) {
        LoginUserBean loginUser = LoginUserUtils.getLoginUser();
        if(!SuperUser.ADMINISTRATOR_NAME.equals(loginUser.getLoginName())){
            auditConfigurationQuery.setDepartmentIds(departmentService.findIdsByCompanyId(loginUser.getCompanyBean().getId()));
        }
    }

    @ApiOperation("根据id获取审核配置")
    @GetMapping("/{auditConfigurationId}")
    @RequiresPermissions("expense:auditConfiguration:get")
    public Result findById(@PathVariable("auditConfigurationId") String auditConfigurationId, FieldsView fieldsView) {
        AuditConfiguration auditConfiguration = auditConfigurationService.findById(auditConfigurationId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(auditConfiguration, fieldsView));
    }

    @ApiOperation("根据用户id数组获取用户列表")
    @GetMapping("/user")
    @RequiresPermissions("expense:auditConfiguration:user")
    public Result findUsersByUserIds(String[] userIds, FieldsView fieldsView) {
        if(userIds==null||userIds.length==0){
            return Result.getSuccess();
        }
        List<SysUser> users = userService.findByIds(userIds);
        return Result.getSuccess(ObjectToMapUtils.listToMap(users, fieldsView));
    }

}
