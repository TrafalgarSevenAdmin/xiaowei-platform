package com.xiaowei.attendancesystem.service.impl;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.xiaowei.account.entity.Company;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.repository.CompanyRepository;
import com.xiaowei.account.repository.SysUserRepository;
import com.xiaowei.attendancesystem.entity.ChiefEngineer;
import com.xiaowei.attendancesystem.entity.PunchRecord;
import com.xiaowei.attendancesystem.repository.ChiefEngineerRepository;
import com.xiaowei.attendancesystem.repository.PunchRecordRepository;
import com.xiaowei.attendancesystem.service.IPunchRecordService;
import com.xiaowei.attendancesystem.status.ChiefEngineerStatus;
import com.xiaowei.attendancesystem.status.PunchRecordStatus;
import com.xiaowei.commonjts.utils.CalculateUtils;
import com.xiaowei.commonjts.utils.GeometryUtil;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.DateUtils;
import com.xiaowei.core.utils.EmptyUtils;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PunchRecordServiceImpl extends BaseServiceImpl<PunchRecord> implements IPunchRecordService {

    @Autowired
    private PunchRecordRepository punchRecordRepository;
    @Autowired
    private SysUserRepository userRepository;
    @Autowired
    private ChiefEngineerRepository chiefEngineerRepository;
    @Autowired
    private CompanyRepository companyRepository;

    public PunchRecordServiceImpl(@Qualifier("punchRecordRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public PunchRecord savePunchRecord(PunchRecord punchRecord, Geometry shape) {
        //1.判断是否有打卡记录(打卡记录由定时任务自动生成),若有,则修改,若没有,则新建再修改
        //2.判断是否在规定范围内
        //3.判断是上班打卡还是下班打卡
        //   若是上班打卡,则判断当前次数是否是0次
        //                 判断是否迟到
        //   若是下班打卡,则判断当前次数是否是1次
        //   若都不是,则证明是在时间范围外打卡,抛出异常
        PunchRecord currentPunchRecord = judgeIsExist(punchRecord);
        Object[] datas = judgeWithinRange(currentPunchRecord, shape);
        Integer status = judgePunchTime(currentPunchRecord, (ChiefEngineer) datas[0]);
        boolean isTrue = (boolean) datas[1];
        if (status == 1) {
            //是否正常打卡
            if (isTrue) {
                currentPunchRecord.setOnPunchRecordStatus(PunchRecordStatus.NORMAL);
            } else {
                currentPunchRecord.setOnPunchRecordStatus(PunchRecordStatus.EXCEPTION);
            }
            currentPunchRecord.setOnPunchFileStore(punchRecord.getPunchFileStore());
        } else {
            //是否正常打卡
            if (isTrue) {
                currentPunchRecord.setOffPunchRecordStatus(PunchRecordStatus.NORMAL);
            } else {
                currentPunchRecord.setOffPunchRecordStatus(PunchRecordStatus.EXCEPTION);
            }
            currentPunchRecord.setOffPunchFileStore(punchRecord.getPunchFileStore());
        }
        return punchRecordRepository.save(currentPunchRecord);
    }

    /**
     * 查询一个公司下所有人某个月份的打卡记录
     *
     * @param companyId
     * @param selectMonth
     * @return
     */
    @Override
    public List<PunchRecord> findByCompanyIdAndMonth(String companyId, Date selectMonth) throws Exception {
        Optional<Company> optional = companyRepository.findById(companyId);
        EmptyUtils.assertOptional(optional, "没有查询到该公司");
        List<SysUser> users = userRepository.findByCompanyId(companyId);
        if (CollectionUtils.isEmpty(users)) {
            throw new BusinessException("该公司下没有任何员工");
        }
        val cal = Calendar.getInstance();
        cal.setTime(selectMonth);
        val firstDayOfMonth = DateUtils.getFirstDayOfMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
        val lastDayOfMonth = DateUtils.getLastDayOfMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
        val formatter = new SimpleDateFormat("yyyy-MM-dd");

        return punchRecordRepository.findByUserIdsBetweenPunchTime(users.stream().map(SysUser::getId).collect(Collectors.toSet()),
                formatter.parse(firstDayOfMonth), formatter.parse(lastDayOfMonth));
    }

    /**
     * 判断是上班打卡还是下班打卡
     *
     * @param currentPunchRecord
     * @param chiefEngineer
     */
    private Integer judgePunchTime(PunchRecord currentPunchRecord, ChiefEngineer chiefEngineer) {
        int stauts = 0;
        //   若是上班打卡,则判断当前次数是否是0次
        //                 判断是否迟到
        //   若是下班打卡,则判断当前次数是否是1次
        //   若都不是,则证明是在时间范围外打卡,抛出异常
        Calendar calendar = Calendar.getInstance();
        Time currentTime = new Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        if (chiefEngineer.getBeginClockInTime().compareTo(currentTime) == -1 && chiefEngineer.getEndClockInTime().compareTo(currentTime) == 1) {
            //上班打卡
            //判断是否已经上班打卡
            if (currentPunchRecord.getPunchCount() != 0) {
                throw new BusinessException("已经上班打卡!");
            }
            currentPunchRecord.setClockInTime(currentTime);
            currentPunchRecord.setPunchCount(1);//当天打卡次数为1
            if (chiefEngineer.getBeLateTime().compareTo(currentTime) == -1) {
                //迟到
                currentPunchRecord.setBeLate(true);
            }
            stauts = 1;
        } else if (chiefEngineer.getBeginClockOutTime().compareTo(currentTime) == -1 && chiefEngineer.getEndClockOutTime().compareTo(currentTime) == 1) {
            //下班打卡
            //判断是否已经下班打卡
//            if (currentPunchRecord.getPunchCount() != 1) {
//                throw new BusinessException("已经下班打卡!");
//            }
            currentPunchRecord.setClockOutTime(currentTime);
            Integer punchCount = currentPunchRecord.getPunchCount();
            if (punchCount == null) {
                punchCount = 0;
            }
            if (punchCount != 2) {
                currentPunchRecord.setPunchCount(punchCount + 1);
            }
            stauts = 2;
        } else {
            //非打卡时间
            throw new BusinessException("现在是非打卡时间!");
        }
        return stauts;
    }

    /**
     * 判断是否在规定范围内
     *
     * @param currentPunchRecord
     * @param shape
     */
    private Object[] judgeWithinRange(PunchRecord currentPunchRecord, Geometry shape) {
        //chiefEngineers 当前用户的办公点集合
        List<ChiefEngineer> chiefEngineers = chiefEngineerRepository.findByUserId(currentPunchRecord.getSysUser().getId());
        if (CollectionUtils.isEmpty(chiefEngineers)) {
            throw new BusinessException("没有查询到任何打卡点");
        }

        Double shortest = 0.00;
        ChiefEngineer defaultChief = null;
        for (int i = 0; i < chiefEngineers.size(); i++) {
            ChiefEngineer chiefEngineer = chiefEngineers.get(i);
            //用户当前位置和打卡点位置的距离
            double v = CalculateUtils.GetDistance(GeometryUtil.getGps((Point) shape),
                    GeometryUtil.getGps((Point) chiefEngineer.getShape())) * 1000;

            Integer distance = chiefEngineer.getDistance();
            if (distance == null) {
                distance = 500;//默认500米
            }
            //判断是否正常
            if (ChiefEngineerStatus.NORMAL.getStatus().equals(chiefEngineer.getStatus())) {
                if (v < distance) {
                    return new Object[]{chiefEngineer, true};
                } else {
                    if (shortest < v - distance) {//验算最小距离
                        shortest = v - distance;
                        defaultChief = chiefEngineer;
                    }
                }
            }

            if (i == chiefEngineers.size() - 1) {//如果是最后一次
                return new Object[]{defaultChief, false};
//                throw new BusinessException("您未到达打卡范围,距离:" + String.format("%.2f", shortest) + "米");
            }
        }
        return new Object[]{defaultChief, true};
    }

    /**
     * 判断是否有打卡记录(打卡记录由定时任务自动生成),若有,则返回;若没有,则新建,再返回
     *
     * @param punchRecord
     */
    private PunchRecord judgeIsExist(PunchRecord punchRecord) {
        SysUser sysUser = punchRecord.getSysUser();
        EmptyUtils.assertObject(sysUser, "打卡人为空");
        EmptyUtils.assertString(sysUser.getId(), "打卡人id为空");
        EmptyUtils.assertOptional(userRepository.findById(sysUser.getId()), "没有查询到打卡人");
        PunchRecord currentPunchRecord = punchRecordRepository.findByUserIdAndCurrentDate(sysUser.getId());
        if (currentPunchRecord != null) {
            return currentPunchRecord;
        } else {
            return punchRecordRepository.save(new PunchRecord(sysUser));
        }
    }
}
