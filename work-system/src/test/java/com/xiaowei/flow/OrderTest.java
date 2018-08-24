package com.xiaowei.flow;

import com.xiaowei.worksystem.receiver.OrderPayedReceiver;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = WorkSystemApplication.class)
public class OrderTest {

    @Autowired
    OrderPayedReceiver orderPayedReceiver;

    @Test
    public void test() {
        orderPayedReceiver.messageReceiver("2c918082656976b1016569951efb006f");
    }
}
