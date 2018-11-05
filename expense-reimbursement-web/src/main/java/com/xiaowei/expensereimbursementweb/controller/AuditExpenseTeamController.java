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
import com.xiaowei.expensereimbursement.entity.AuditExpenseTeam;
import com.xiaowei.expensereimbursement.service.IAuditExpenseTeamService;
import com.xiaowei.expensereimbursementweb.dto.AuditExpenseTeamDTO;
import com.xiaowei.expensereimbursementweb.query.AuditExpenseTeamQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 审核小组管理
 */
@Api(tags = "审核小组接口")
@RestController
@RequestMapping("/api/team")
public class AuditExpenseTeamController {

    @Autowired
    private IAuditExpenseTeamService auditExpenseTeamService;


    @ApiOperation(value = "添加审核小组")
    @AutoErrorHandler
    @PostMapping("")
    @HandleLog(type = "添加审核小组", contentParams = {@ContentParam(useParamField = true, field = "auditExpenseTeamDTO", value = "审核小组信息")})
    public Result insert(@RequestBody @Validated(V.Insert.class) AuditExpenseTeamDTO auditExpenseTeamDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        AuditExpenseTeam auditExpenseTeam = BeanCopyUtils.copy(auditExpenseTeamDTO, AuditExpenseTeam.class);
        auditExpenseTeam = auditExpenseTeamService.saveAuditExpenseTeam(auditExpenseTeam);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(auditExpenseTeam, fieldsView));
    }

    @ApiOperation(value = "修改审核小组")
    @AutoErrorHandler
    @PutMapping("/{teamId}")
    @HandleLog(type = "修改审核小组", contentParams = {@ContentParam(useParamField = true, field = "auditExpenseTeamDTO", value = "审核小组信息"),
            @ContentParam(useParamField = false, field = "teamId", value = "审核小组id")})
    public Result update(@PathVariable("teamId") String teamId, @RequestBody @Validated(V.Update.class) AuditExpenseTeamDTO auditExpenseTeamDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        AuditExpenseTeam auditExpenseTeam = BeanCopyUtils.copy(auditExpenseTeamDTO, AuditExpenseTeam.class);
        auditExpenseTeam.setId(teamId);
        auditExpenseTeam = auditExpenseTeamService.updateAuditExpenseTeam(auditExpenseTeam);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(auditExpenseTeam, fieldsView));
    }

    @ApiOperation("审核小组查询接口")
    @GetMapping("")
    public Result query(AuditExpenseTeamQuery auditExpenseTeamQuery, FieldsView fieldsView) {
        //查询审核小组设置默认条件
        setDefaultCondition(auditExpenseTeamQuery);

        if (auditExpenseTeamQuery.isNoPage()) {
            List<AuditExpenseTeam> auditExpenseTeams = auditExpenseTeamService.query(auditExpenseTeamQuery, AuditExpenseTeam.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(auditExpenseTeams, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = auditExpenseTeamService.queryPage(auditExpenseTeamQuery, AuditExpenseTeam.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(AuditExpenseTeamQuery auditExpenseTeamQuery) {

    }

    @ApiOperation("根据id获取审核小组")
    @GetMapping("/{teamId}")
    public Result findById(@PathVariable("teamId") String teamId, FieldsView fieldsView) {
        AuditExpenseTeam auditExpenseTeam = auditExpenseTeamService.findById(teamId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(auditExpenseTeam, fieldsView));
    }

    @ApiOperation("删除审核小组")
    @DeleteMapping("/{teamId}")
    @RequiresPermissions("expense:expenseForm:delete")
    @HandleLog(type = "删除审核小组", contentParams = {@ContentParam(useParamField = false, field = "teamId", value = "审核小组id")})
    public Result delete(@PathVariable("teamId") String teamId) {
        auditExpenseTeamService.deleteAuditExpenseTeam(teamId);
        return Result.getSuccess();
    }

}
