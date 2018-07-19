package com.xiaowei.attendancesystem.controller;

import com.xiaowei.attendancesystem.dto.PunchRecordDTO;
import com.xiaowei.attendancesystem.entity.PunchRecord;
import com.xiaowei.attendancesystem.service.IPunchRecordService;
import com.xiaowei.commonjts.utils.GeometryUtil;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
