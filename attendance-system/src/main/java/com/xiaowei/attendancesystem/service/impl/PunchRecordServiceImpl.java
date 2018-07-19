package com.xiaowei.attendancesystem.service.impl;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.repository.SysUserRepository;
import com.xiaowei.attendancesystem.entity.ChiefEngineer;
import com.xiaowei.attendancesystem.entity.PunchRecord;
import com.xiaowei.attendancesystem.repository.ChiefEngineerRepository;
import com.xiaowei.attendancesystem.repository.PunchRecordRepository;
import com.xiaowei.attendancesystem.service.IPunchRecordService;
import com.xiaowei.commonjts.utils.CalculateUtils;
import com.xiaowei.commonjts.utils.GeometryUtil;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

@Service
public class PunchRecordServiceImpl extends BaseServiceImpl<PunchRecord> implements IPunchRecordService {

    @Autowired
    private PunchRecordRepository punchRecordRepository;
    @Autowired
    private SysUserRepository userRepository;
    @Autowired
    private ChiefEngineerRepository chiefEngineerRepository;

    @Value("${punch.distance}")
    private Double distance;

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
        ChiefEngineer chiefEngineer = judgeWithinRange(currentPunchRecord, shape);
        judgePunchTime(currentPunchRecord, chiefEngineer);
        return punchRecordRepository.save(currentPunchRecord);
    }

    /**
     * 判断是上班打卡还是下班打卡
     *
     * @param currentPunchRecord
     * @param chiefEngineer
     */
    private void judgePunchTime(PunchRecord currentPunchRecord, ChiefEngineer chiefEngineer) {
        //   若是上班打卡,则判断当前次数是否是0次
        //                 判断是否迟到
        //   若是下班打卡,则判断当前次数是否是1次
        //   若都不是,则证明是在时间范围外打卡,抛出异常
        Calendar calendar = Calendar.getInstance();
        Time currentTime = new Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        if (chiefEngineer.getBeginClockInTime().compareTo(currentTime) == -1 && chiefEngineer.getEndClockInTime().compareTo(currentTime) == 1) {
            //上班打卡
            currentPunchRecord.setClockInTime(currentTime);
            currentPunchRecord.setPunchCount(1);//当天打卡次数为1
            if (chiefEngineer.getBeLateTime().compareTo(currentTime) == -1) {
                //迟到
                currentPunchRecord.setBeLate(true);
            }
        } else if (chiefEngineer.getBeginClockOutTime().compareTo(currentTime) == -1 && chiefEngineer.getEndClockOutTime().compareTo(currentTime) == 1) {
            //下班打卡
            currentPunchRecord.setClockOutTime(currentTime);
            Integer punchCount = currentPunchRecord.getPunchCount();
            if(punchCount == null){
                punchCount = 0;
            }
            currentPunchRecord.setPunchCount(punchCount + 1);
        } else {
            //非打卡时间
            throw new BusinessException("现在是非打卡时间!");
        }
    }

    /**
     * 判断是否在规定范围内
     *
     * @param currentPunchRecord
     * @param shape
     */
    private ChiefEngineer judgeWithinRange(PunchRecord currentPunchRecord, Geometry shape) {
        //chiefEngineers 当前用户的办公点集合
        List<ChiefEngineer> chiefEngineers = chiefEngineerRepository.findByUserId(currentPunchRecord.getSysUser().getId());
        Double shortest = 0.00;
        for (int i = 0; i < chiefEngineers.size(); i++) {
            ChiefEngineer chiefEngineer = chiefEngineers.get(i);
            double v = CalculateUtils.GetDistance(GeometryUtil.getGps((Point) shape),
                    GeometryUtil.getGps((Point) chiefEngineer.getShape())) * 1000;
            if (v < distance) {
                return chiefEngineer;
            }
            if (shortest < v - distance) {//验算最小距离
                shortest = v - distance;
            }
            if (i == chiefEngineers.size() - 1) {//如果是最后一次
                throw new BusinessException("您未到达打卡范围,距离:" + String.format("%.2f", shortest) + "米");
            }
        }
        return chiefEngineers.get(0);
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
