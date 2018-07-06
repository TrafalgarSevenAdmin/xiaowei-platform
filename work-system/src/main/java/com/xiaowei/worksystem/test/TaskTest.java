package com.xiaowei.worksystem.test;

import com.xiaowei.WorkSystemApplication;
import com.xiaowei.worksystem.utils.ServiceItemUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Lenovo on 2017/10/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WorkSystemApplication.class)
public class TaskTest {

    @Test
    public void test(){
        System.out.println(ArrayUtils.contains(ServiceItemUtils.isDone, 2));
        System.out.println(ArrayUtils.contains(ServiceItemUtils.isDone, 3));
        System.out.println(ArrayUtils.contains(ServiceItemUtils.isDone, 4));
        System.out.println(ArrayUtils.contains(ServiceItemUtils.isDone, 5));
    }
}
