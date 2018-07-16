package com.xiaowei;

import com.xiaowei.mq.bean.TaskMessage;
import com.xiaowei.mq.constant.MqQueueConstant;
import com.xiaowei.mq.sender.MessagePushSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WxMpApplication.class)
public class TaskTest {

    @Autowired
    MessagePushSender messagePushSender;

    @Test
    public void test() throws InterruptedException {
            messagePushSender.sendDelayTask(new TaskMessage(), 5000);
            Thread.sleep(8000);
    }
}
