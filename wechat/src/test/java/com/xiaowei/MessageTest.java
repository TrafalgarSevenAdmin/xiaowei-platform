package com.xiaowei;

import com.xiaowei.mq.sender.MessagePushSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = WxMpApplication.class)
public class MessageTest {

    @Autowired
    MessagePushSender messagePushSender;

    @Test
    public void testSendMessage() {
        messagePushSender.sendOrderPayedMessage("order_default_payed_queue", "123456");
    }
}
