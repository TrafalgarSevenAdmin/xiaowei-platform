package com.xiaowei.attendancesystem.test;

import com.xiaowei.AttendanceSystemApplication;
import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.attendancesystem.repository.ChiefEngineerRepository;
import com.xiaowei.attendancesystem.repository.PunchRecordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Lenovo on 2017/10/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AttendanceSystemApplication.class)
public class TaskTest {

    @Autowired
    private PunchRecordRepository punchRecordRepository;
    @Autowired
    private ChiefEngineerRepository chiefEngineerRepository;

    @Autowired
    private ISysUserService userService;

    @Test
    public void test(){
//        PunchRecord byUserIdAndCurrentDate = punchRecordRepository.findByUserIdAndCurrentDate("1");
//        List<ChiefEngineer> chiefEngineers = chiefEngineerRepository.findByUserId("1");
//        final List<SysUser> fromCompanys = userService.findFromCompanys();
    }



}
