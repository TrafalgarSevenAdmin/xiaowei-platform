package com.xiaowei.expensereimbursementweb.test;


import com.alibaba.fastjson.JSONArray;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.expensereimbursement.bean.AccountContentBean;
import com.xiaowei.expensereimbursement.entity.ExpenseAccount;
import com.xiaowei.expensereimbursementweb.dto.ExpenseAccountDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/10/16.
 */
public class SimpleTest {


    @Test
    public  void test(){
        ExpenseAccountDTO expenseAccountDTO = new ExpenseAccountDTO();
        expenseAccountDTO.setAccountName("测试数据名称");
        List<AccountContentBean> accountContentBeans = new ArrayList<>();
        final AccountContentBean accountContentBean = new AccountContentBean();
        accountContentBean.setDict("dict");
        accountContentBean.setKey("key");
        accountContentBean.setType("type");
        accountContentBean.setValue("value");
        accountContentBeans.add(accountContentBean);
        expenseAccountDTO.setAccountContentBeans(accountContentBeans);
        ExpenseAccount expenseAccount = BeanCopyUtils.copy(expenseAccountDTO, ExpenseAccount.class);
        expenseAccount.setAccountContent(JSONArray.toJSONString(expenseAccountDTO.getAccountContentBeans()));

    }


}
