package com.xiaowei.expensereimbursementweb.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaowei.account.service.ISysUserService;
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
import com.xiaowei.expensereimbursement.entity.ReimbursementStandard;
import com.xiaowei.expensereimbursement.service.IReimbursementStandardService;
import com.xiaowei.expensereimbursementweb.dto.AllReimbursementStandardDTO;
import com.xiaowei.expensereimbursementweb.dto.ReimbursementStandardDTO;
import com.xiaowei.expensereimbursementweb.query.ReimbursementStandardQuery;
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
 * 费用报销标准管理
 */
@Api(tags = "费用报销标准接口")
@RestController
@RequestMapping("/api/standard")
public class ReimbursementStandardController {

    @Autowired
    private IReimbursementStandardService reimbursementStandardService;
    @Autowired
    private ISysUserService userService;

    @ApiOperation(value = "添加费用报销标准")
    @AutoErrorHandler
    @PostMapping("")
    @RequiresPermissions("expense:standard:add")
    @HandleLog(type = "添加费用报销标准", contentParams = {@ContentParam(useParamField = true, field = "reimbursementStandardDTO", value = "费用报销标准信息")})
    public Result insert(@RequestBody @Validated(V.Insert.class) ReimbursementStandardDTO reimbursementStandardDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        ReimbursementStandard reimbursementStandard = BeanCopyUtils.copy(reimbursementStandardDTO, ReimbursementStandard.class);
        reimbursementStandard = reimbursementStandardService.saveReimbursementStandard(reimbursementStandard);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(reimbursementStandard, fieldsView));
    }

    @ApiOperation(value = "添加多条费用报销标准")
    @AutoErrorHandler
    @PostMapping("/all")
    @RequiresPermissions("expense:standard:add")
    @HandleLog(type = "添加多条费用报销标准", contentParams = {@ContentParam(useParamField = true, field = "allReimbursementStandardDTO", value = "费用报销标准信息")})
    public Result insertAll(@RequestBody @Validated(V.Insert.class) AllReimbursementStandardDTO allReimbursementStandardDTO, BindingResult bindingResult) throws Exception {
        ReimbursementStandard reimbursementStandard = new ReimbursementStandard();
        reimbursementStandard.setUnitCost(allReimbursementStandardDTO.getUnitCost());
        reimbursementStandard.setStartCity(allReimbursementStandardDTO.getStartCity());
        reimbursementStandard.setEndCity(allReimbursementStandardDTO.getEndCity());
        reimbursementStandard.setSubjectCode(allReimbursementStandardDTO.getSubjectCode());
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(reimbursementStandard));
        List<JSONObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(jsonObject);
        jsonObjects = FastJsonUtils.transArrToArrMap(allReimbursementStandardDTO.getPostLevels(),"postLevel",jsonObjects);
        jsonObjects = FastJsonUtils.transArrToArrMap(allReimbursementStandardDTO.getShipLevels(),"shipLevel",jsonObjects);
        jsonObjects = FastJsonUtils.transArrToArrMap(allReimbursementStandardDTO.getCityLevels(),"cityLevel",jsonObjects);
        List<ReimbursementStandard> reimbursementStandards = FastJsonUtils.listJsonObjectToListObject(jsonObjects, ReimbursementStandard.class);
        reimbursementStandardService.save(reimbursementStandards);
        return Result.getSuccess();
    }

    @ApiOperation(value = "修改费用报销标准")
    @AutoErrorHandler
    @PutMapping("/{standardId}")
    @RequiresPermissions("expense:standard:update")
    @HandleLog(type = "修改费用报销标准", contentParams = {@ContentParam(useParamField = true, field = "allReimbursementStandardDTO", value = "费用报销标准信息"),
            @ContentParam(useParamField = false, field = "standardId", value = "费用报销标准id")})
    public Result update(@PathVariable("standardId") String standardId, @RequestBody @Validated(V.Update.class) ReimbursementStandardDTO reimbursementStandardDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        ReimbursementStandard reimbursementStandard = BeanCopyUtils.copy(reimbursementStandardDTO, ReimbursementStandard.class);
        reimbursementStandard.setId(standardId);
        reimbursementStandard = reimbursementStandardService.updateReimbursementStandard(reimbursementStandard);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(reimbursementStandard, fieldsView));
    }

    @ApiOperation(value = "删除费用报销标准")
    @DeleteMapping("/{standardId}")
    @RequiresPermissions("expense:standard:delete")
    @HandleLog(type = "修改费用报销标准", contentParams = {@ContentParam(useParamField = false, field = "standardId", value = "费用报销标准id")})
    public Result delete(@PathVariable("standardId") String standardId) throws Exception {
        reimbursementStandardService.deleteReimbursementStandard(standardId);
        return Result.getSuccess();
    }

    @ApiOperation("费用报销标准查询接口")
    @GetMapping("")
    @RequiresPermissions("expense:standard:query")
    public Result query(ReimbursementStandardQuery reimbursementStandardQuery, FieldsView fieldsView) {
        //查询费用报销标准设置默认条件
        setDefaultCondition(reimbursementStandardQuery);

        if (reimbursementStandardQuery.isNoPage()) {
            List<ReimbursementStandard> auditConfigurations = reimbursementStandardService.query(reimbursementStandardQuery, ReimbursementStandard.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(auditConfigurations, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = reimbursementStandardService.queryPage(reimbursementStandardQuery, ReimbursementStandard.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(ReimbursementStandardQuery reimbursementStandardQuery) {

    }

    @ApiOperation("根据id获取费用报销标准")
    @GetMapping("/{standardId}")
    @RequiresPermissions("expense:standard:get")
    public Result findById(@PathVariable("standardId") String standardId, FieldsView fieldsView) {
        ReimbursementStandard reimbursementStandard = reimbursementStandardService.findById(standardId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(reimbursementStandard, fieldsView));
    }

    @ApiOperation("根据岗位级别查询舱位级别")
    @GetMapping("/shipLevel")
    @RequiresPermissions("expense:standard:shipLevel")
    public Result findShipLevelByPostLevel(@RequestParam("postLevel") String postLevel,
                                           @RequestParam("subjectCode") String subjectCode) {
        List<String> shipLevels = reimbursementStandardService.findShipLevelByPostLevel(postLevel, subjectCode);
        return Result.getSuccess(shipLevels);
    }

    @ApiOperation("根据岗位级别查询城市级别")
    @GetMapping("/cityLevel")
    @RequiresPermissions("expense:standard:cityLevel")
    public Result findCityLevelByPostLevel(@RequestParam("postLevel") String postLevel,
                                           @RequestParam("subjectCode") String subjectCode) {
        List<String> cityLevels = reimbursementStandardService.findCityLevelByPostLevel(postLevel, subjectCode);
        return Result.getSuccess(cityLevels);
    }


}
