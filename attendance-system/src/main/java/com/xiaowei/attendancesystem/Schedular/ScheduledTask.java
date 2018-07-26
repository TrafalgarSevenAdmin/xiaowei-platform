package com.xiaowei.attendancesystem.Schedular;

import com.xiaowei.account.repository.SysUserRepository;
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
    private SysUserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);
    /**
     * 每天早上1点创建当天所有公司的所有员工的考勤记录
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void clearWeather() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("批量创建当天考勤记录 {}", dateFormat.format(new Date()));
    }
}
