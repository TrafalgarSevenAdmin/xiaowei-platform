package com.xiaowei.attendancesystem.controller;

import com.xiaowei.attendancesystem.dto.PunchFormDTO;
import com.xiaowei.attendancesystem.dto.PunchRecordDTO;
import com.xiaowei.attendancesystem.entity.PunchRecord;
import com.xiaowei.attendancesystem.query.PunchRecordQuery;
import com.xiaowei.attendancesystem.service.IPunchRecordService;
import com.xiaowei.commonjts.utils.GeometryUtil;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "打卡记录接口")
@RestController
@RequestMapping("/api/punch")
public class PunchRecordController {

    @Autowired
    private IPunchRecordService punchRecordService;

    @ApiOperation(value = "添加打卡记录")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) PunchRecordDTO punchRecordDTO,
                         BindingResult bindingResult,
                         FieldsView fieldsView) throws Exception {
        PunchRecord punchRecord = BeanCopyUtils.copy(punchRecordDTO, PunchRecord.class);
        punchRecord = punchRecordService.savePunchRecord(punchRecord,
                GeometryUtil.transWKT(punchRecordDTO.getWkt()));
        return Result.getSuccess(ObjectToMapUtils.objectToMap(punchRecord, fieldsView));
    }

    @ApiOperation("打卡记录查询接口")
    @GetMapping("")
    public Result query(PunchRecordQuery punchRecordQuery, FieldsView fieldsView) {
        //查询办公点设置默认条件
        setDefaultCondition(punchRecordQuery);
        if (punchRecordQuery.isNoPage()) {
            List<PunchRecord> punchRecords = punchRecordService.query(punchRecordQuery, PunchRecord.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(punchRecords, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = punchRecordService.queryPage(punchRecordQuery, PunchRecord.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(PunchRecordQuery punchRecordQuery) {

    }

    @ApiOperation("打卡记录报表统计")
    @GetMapping("/form")
    @AutoErrorHandler
    public Result punchForm(@Validated PunchFormDTO punchFormDTO,BindingResult bindingResult) throws Exception {
        //查询一个公司下所有人某个月份的打卡记录
        List<PunchRecord> punchRecords = punchRecordService.findByCompanyIdAndMonth(punchFormDTO.getCompanyId(),punchFormDTO.getSelectMonth());

        return Result.getSuccess(getPunchFormMap(punchRecords));
    }

    private Map<String,Object> getPunchFormMap(List<PunchRecord> punchRecords) {
        return null;
    }

}
