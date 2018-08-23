package com.xiaowei.accountweb.test;

import com.xiaowei.AccountWebApplication;
import com.xiaowei.commonupload.utils.CheckFiledUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Lenovo on 2017/10/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AccountWebApplication.class)
public class TaskTest {

    @Autowired
    private CheckFiledUtils checkFiledUtils;

    @Test
    public void test(){
        String[] names = checkFiledUtils.getNames();
    }

}
