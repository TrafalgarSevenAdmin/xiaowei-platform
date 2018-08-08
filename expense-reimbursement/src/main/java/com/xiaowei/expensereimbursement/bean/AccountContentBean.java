package com.xiaowei.expensereimbursement.bean;

import lombok.Data;

@Data
public class AccountContentBean {
    private String key;
    private String value;
    private String label;
    private String dict;
    private String type;
    private Boolean required;
}
