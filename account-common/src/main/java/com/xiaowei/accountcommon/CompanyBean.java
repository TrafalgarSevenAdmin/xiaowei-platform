package com.xiaowei.accountcommon;

import java.io.Serializable;

public class CompanyBean implements Serializable{
    private String id;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 编号
     */
    private String code;
    /**
     * 地址
     */
    private String address;
    /**
     * 状态:0表示正常,1表示停用,99代表删除
     */
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
