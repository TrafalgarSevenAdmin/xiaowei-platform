package com.xiaowei.attendancesystem.controller;

import com.xiaowei.account.entity.Company;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.repository.SysUserRepository;
import com.xiaowei.account.service.ICompanyService;
import com.xiaowei.attendancesystem.bean.PunchFormCountBean;
import com.xiaowei.attendancesystem.dto.PunchFormDTO;
import com.xiaowei.attendancesystem.dto.PunchRecordDTO;
import com.xiaowei.attendancesystem.entity.PunchRecord;
import com.xiaowei.attendancesystem.query.PunchRecordQuery;
import com.xiaowei.attendancesystem.service.IPunchRecordService;
import com.xiaowei.attendancesystem.status.PunchRecordType;
import com.xiaowei.attendancesystem.utils.PunchMonthExcelUtils;
import com.xiaowei.commonjts.utils.GeometryUtil;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.DateUtils;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "打卡记录接口")
@RestController
@RequestMapping("/api/punch")
public class PunchRecordController {

    @Autowired
    private IPunchRecordService punchRecordService;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private SysUserRepository userRepository;

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

    @ApiOperation(value = "修改上班打卡状态")
    @AutoErrorHandler
    @PutMapping("/{punchRecordId}/on/status")
    public Result updateOnStatus(@PathVariable("punchRecordId") String punchRecordId,
                               @RequestBody @Validated(PunchRecordDTO.UpdateOnStatus.class) PunchRecordDTO punchRecordDTO,
                               BindingResult bindingResult,
                               FieldsView fieldsView) throws Exception {
        PunchRecord punchRecord = BeanCopyUtils.copy(punchRecordDTO, PunchRecord.class);
        punchRecord.setId(punchRecordId);
        punchRecord = punchRecordService.updateOnStatus(punchRecord);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(punchRecord, fieldsView));
    }

    @ApiOperation(value = "修改下班打卡状态")
    @AutoErrorHandler
    @PutMapping("/{punchRecordId}/off/status")
    public Result updateOffStatus(@PathVariable("punchRecordId") String punchRecordId,
                                 @RequestBody @Validated(PunchRecordDTO.UpdateOffStatus.class) PunchRecordDTO punchRecordDTO,
                                 BindingResult bindingResult,
                                 FieldsView fieldsView) throws Exception {
        PunchRecord punchRecord = BeanCopyUtils.copy(punchRecordDTO, PunchRecord.class);
        punchRecord.setId(punchRecordId);
        punchRecord = punchRecordService.updateOffStatus(punchRecord);
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
        //查询一个公司下所有人某个月份的打卡记录
        List<PunchRecord> punchRecords = punchRecordService.findByCompanyIdAndMonth(punchFormDTO.getCompanyId(), punchFormDTO.getSelectMonth());
        final Company company = companyService.findById(punchFormDTO.getCompanyId());
        final List<SysUser> users = userRepository.findByCompanyId(company.getId());
        List<Object[]> totalDatas = new ArrayList<>();
        //获取当前天的时间格式化对象
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        users.stream().forEach(sysUser -> {
            List<Object> datas = new ArrayList<>();
            datas.add("");
            datas.add(sysUser.getNickName());
            final List<PunchRecord> userPunchs = punchRecords.stream().
                    filter(punchRecord -> punchRecord.getSysUser().getId().equals(sysUser.getId()))
                    .collect(Collectors.toList());
            //本月的最大天数
            final Integer lastDayOfMonth = DateUtils.getLastDayOfMonth(punchFormDTO.getSelectMonth());
            for (int i = 1; i < lastDayOfMonth + 1; i++) {
                //过滤出当天打卡记录 如果没有 则输出"没有记录" 如果有,则判断打卡情况
                int finalI = i;
                final Optional<PunchRecord> optionalRecord = userPunchs.stream()
                        .filter(punchRecord -> Integer.valueOf(simpleDateFormat.format(punchRecord.getPunchDate())) == finalI)
                        .findFirst();
                if (optionalRecord.isPresent()) {
                    datas.add(judgePunchRecordType(optionalRecord.get()));
                } else {
                    datas.add(PunchRecordType.NOPUNCH);
                }
            }
            totalDatas.add(datas.toArray());
        });
        new PunchMonthExcelUtils(company.getCompanyName() + new SimpleDateFormat("yyyy年MM月").format(punchFormDTO.getSelectMonth()) + "考勤", totalDatas, response).export();

    }

    private PunchRecordType judgePunchRecordType(PunchRecord punchRecord) {
        //判断是否上班未打卡,下班未打卡,上下班均未打卡,上班迟到,上班迟到且下班未打卡
        if (punchRecord.getClockInTime() == null && punchRecord.getClockOutTime() == null) {
            return PunchRecordType.CLOCKINISNULLANDCLOCKOUTISNULL;//上下班均未打卡
        }
        if (punchRecord.getBeLate() && punchRecord.getClockOutTime() == null) {
            return PunchRecordType.BELATEANDCLOCKOUTISNULL;//迟到且下班未打卡
        }
        if (punchRecord.getClockOutTime() == null) {
            return PunchRecordType.CLOCKOUTISNULL;//下班未打卡
        }
        if (punchRecord.getClockInTime() == null) {
            return PunchRecordType.CLOCKINISNULL;//上班未打卡
        }
        if (punchRecord.getBeLate()) {
            return PunchRecordType.BELATE;//迟到
        }
        return PunchRecordType.NORMAL;
    }

}
