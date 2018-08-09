package com.xiaowei.accountweb.test;

import com.xiaowei.AccountWebApplication;
import com.xiaowei.account.entity.TestEntity;
import com.xiaowei.account.repository.TestRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

/**
 * Created by Lenovo on 2017/10/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AccountWebApplication.class)
public class TaskTest {
    @Autowired
    private TestRepository testRepository;

    @Test
    public void testaa(){
        final Optional<TestEntity> byId = testRepository.findById("1");
        final TestEntity testEntity = byId.get();
    }
}
