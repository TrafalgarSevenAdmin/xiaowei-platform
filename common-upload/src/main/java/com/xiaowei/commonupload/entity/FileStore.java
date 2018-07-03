package com.xiaowei.commonupload.entity;


import com.xiaowei.core.basic.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 文件仓库
 */
@Table(name = "sys_file_store")
@Entity
public class FileStore extends BaseEntity {

    /**
     * 原始文件名称
     */
    private String originalFilename;


    /**
     * 文件地址
     */
    private String path;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 上传用户
     */
    private String userId;

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
