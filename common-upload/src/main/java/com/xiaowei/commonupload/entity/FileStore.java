package com.xiaowei.commonupload.entity;


import com.xiaowei.commonupload.utils.UploadConfigUtils;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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

    public String getPath() {
        if (StringUtils.isNotEmpty(this.path)) {
            return UploadConfigUtils.getAccessUrlRoot() + this.path;
        }
        return path;
    }
}
