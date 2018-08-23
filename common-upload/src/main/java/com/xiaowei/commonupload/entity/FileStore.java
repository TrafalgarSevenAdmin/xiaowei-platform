package com.xiaowei.commonupload.entity;


import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 文件仓库
 */
@Table(name = "sys_file_store")
@Entity
@Data
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

    /**
     * 检测时间
     */
    @Temporal(TemporalType.DATE)
    private Date checkDate;

}
