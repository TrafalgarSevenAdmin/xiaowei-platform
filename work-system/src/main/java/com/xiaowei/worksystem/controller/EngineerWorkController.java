package com.xiaowei.worksystem.controller;

import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.worksystem.entity.EngineerWork;
import com.xiaowei.worksystem.service.IEngineerWorkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "工程师处理工单工作日志接口")
@RestController
@RequestMapping("/api/engineer")
public class EngineerWorkController {
    @Autowired
    private IEngineerWorkService engineerWorkService;

    @ApiOperation("根据id获取工单")
    @GetMapping("/{engineerWorkId}")
    public Result findById(@PathVariable("engineerWorkId") String engineerWorkId, FieldsView fieldsView) {
        EngineerWork engineerWork = engineerWorkService.findById(engineerWorkId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(engineerWork, fieldsView));
    }

}
