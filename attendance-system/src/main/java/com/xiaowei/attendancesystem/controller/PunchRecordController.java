package com.xiaowei.attendancesystem.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.xiaowei.attendancesystem.bean.ExcelBean;
import com.xiaowei.attendancesystem.bean.PunchFormCountBean;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
    public Result punchForm(@Validated PunchFormDTO punchFormDTO, BindingResult bindingResult) throws Exception {
        //查询一个公司下所有人某个月份的打卡记录
        List<PunchRecord> punchRecords = punchRecordService.findByCompanyIdAndMonth(punchFormDTO.getCompanyId(), punchFormDTO.getSelectMonth());

        return Result.getSuccess(getPunchFormMap(punchRecords));
    }

    private Map<String, Object> getPunchFormMap(List<PunchRecord> punchRecords) {
        Map<String, Object> punchMap = new HashMap<>();
        //迟到次数
        long belateCount = punchRecords.stream().filter(punchRecord -> punchRecord.getBeLate()).count();
        //上班未打卡次数
        long clockInIsNullCount = punchRecords.stream().filter(punchRecord -> punchRecord.getClockInTime() == null).count();
        //下班未打卡次数
        long clockOutIsNullCount = punchRecords.stream().filter(punchRecord -> punchRecord.getClockOutTime() == null).count();
        //迟到率
        float beLateRate = (float) belateCount / (float) punchRecords.size();
        float clockInIsNullRate = (float) clockInIsNullCount / (float) punchRecords.size();
        float clockOutIsNullRate = (float) clockOutIsNullCount / (float) punchRecords.size();
        punchMap.put("punchFormCount", new PunchFormCountBean[]{new PunchFormCountBean(belateCount, clockInIsNullCount, clockOutIsNullCount)});
        punchMap.put("beLateRate", beLateRate);
        punchMap.put("clockInIsNullRate", clockInIsNullRate);
        punchMap.put("clockOutIsNullRate", clockOutIsNullRate);
        return punchMap;
    }

    @ApiOperation(value = "导出详细报表")
    @GetMapping("/downLoad")
    @AutoErrorHandler
    public void download(@Validated PunchFormDTO punchFormDTO, BindingResult bindingResult,
                         HttpServletResponse response) throws Exception {
//            InputStream inputStream = new FileInputStream("E:/picture/红心海贼团.jpg");
//            if(inputStream == null){
//                throw new FileNotFoundException();
//            }
//            int length  = inputStream.available();
//            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
//            MultiValueMap<String,String> valueMap = new HttpHeaders();
//            String fileName = URLEncoder.encode("红心海贼团.jps", "UTF-8");
//            valueMap.put("Content-Disposition", Arrays.asList(new String[]{"attachment;filename=" + fileName}));
//            valueMap.put("Content-Length", Arrays.asList(new String[]{length + ""}));
//            return new ResponseEntity<byte[]>(bytes,valueMap,HttpStatus.OK);
        List<ExcelBean> excelBeans = new ArrayList<>();
        ExcelBean excelBean = new ExcelBean();
        excelBean.setBirthday(new Date());
        excelBean.setName("袁玮");
        excelBean.setRegistrationDate(new Date());
        excelBean.setSex(1);
        excelBeans.add(excelBean);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("计算机一班学生", "学生"),
                ExcelBean.class, excelBeans);
        String fileName=new String(("xxx" + ".xls").getBytes("utf-8"),"iso-8859-1"); //解决中文乱码问题
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName+ "\"");

        workbook.write(response.getOutputStream());
    }

}
