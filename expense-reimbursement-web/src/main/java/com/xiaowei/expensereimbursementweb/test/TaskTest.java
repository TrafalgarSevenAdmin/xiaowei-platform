package com.xiaowei.expensereimbursementweb.test;

import com.xiaowei.ExpenseReimbursementWebApplication;
import com.xiaowei.expensereimbursement.repository.ExpenseFormRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Lenovo on 2017/10/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExpenseReimbursementWebApplication.class)
public class TaskTest {

    @Autowired
    private ExpenseFormRepository expenseFormRepository;

    @Test
    public void test(){
        final Long countFirstTrial = expenseFormRepository.findCountFirstTrial("40288136651718190165173631370006", "1");
        System.out.println(countFirstTrial);
    }
}
