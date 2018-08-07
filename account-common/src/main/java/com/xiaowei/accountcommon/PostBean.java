package com.xiaowei.accountcommon;

import lombok.Data;

@Data
public class PostBean {
    /**
     * 编号
     */
    private String code;
    /**
     * 岗位名称
     */
    private String postName;
    /**
     * 岗位状态
     */
    private Integer status;
    /**
     * 简介
     */
    private String intro;
    /**
     * 岗位级别
     */
    private Integer level;
}
