package com.xiaowei.attendancesystem.schedular;

import com.xiaowei.account.consts.UserStatus;
import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.attendancesystem.entity.PunchRecord;
import com.xiaowei.attendancesystem.repository.PunchRecordRepository;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private PunchRecordRepository punchRecordRepository;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    /**
     * 每天早上1点创建当天所有公司的所有员工的考勤记录
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void createAllCurrentDatePunch() {
        val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("批量创建当天考勤记录 {}", dateFormat.format(new Date()));
        val users = userService.findFromCompanys();
        users.stream().forEach(sysUser -> {
            if(UserStatus.NORMAL.getStatus().equals(sysUser.getStatus())){
                if (punchRecordRepository.findByUserIdAndCurrentDate(sysUser.getId()) == null) {
                    punchRecordRepository.save(new PunchRecord(sysUser));
                }
            }
        });
    }
}
