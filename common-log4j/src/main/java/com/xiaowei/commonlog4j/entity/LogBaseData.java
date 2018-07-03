package com.xiaowei.commonlog4j.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yuanxuan on 2018/4/16.
 * 操作日志
 */
@Table(name = "sys_log")
@Entity
public class LogBaseData {
    @Id
    @GeneratedValue(
            generator = "system-uuid"
    )
    @GenericGenerator(
            name = "system-uuid",
            strategy = "uuid"
    )
    private String id;
    /**
     * 操作用户的id
     */
    private String employeeId;
    /**
     * 操作内容
     */
    @Lob
    private String content;
    /**
     * 操作路径
     */
    private String url;
    /**
     * 操作类型
     */
    private String type;

    /**
     * 操作时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date handleTime;
    /**
     * 操作的来源模块
     */
    private String logModule;
    /**
     * ip地址
     */
    private String ip;

    private Boolean success;

    private String cause;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getLogModule() {
        return logModule;
    }

    public void setLogModule(String logModule) {
        this.logModule = logModule;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
