package com.xiaowei.accountcommon;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepartmentBean implements Serializable{
    private String id;
    /**
     * 编号
     */
    private String code;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 状态:0表示正常,1表示停用,99代表删除
     */
    private Integer status;

}
