package com.xiaowei.accountweb.test;

import com.xiaowei.AccountWebApplication;
import com.xiaowei.account.repository.DepartmentRepository;
import com.xiaowei.commonupload.utils.CheckFiledUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Lenovo on 2017/10/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AccountWebApplication.class)
public class TaskTest {

    @Autowired
    private CheckFiledUtils checkFiledUtils;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void test(){
//        String[] names = checkFiledUtils.getNames();
        final List<String> idsByCompanyId = departmentRepository.findIdsByCompanyId("1");
    }

}
