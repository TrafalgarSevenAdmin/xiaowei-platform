package com.xiaowei.commonupload.model;


import com.xiaowei.commonupload.enums.FileType;

/**
 * 约束对象
 */
public class SchemeModel {

    /**
     * 文件类型
     */
    private FileType fileType;

    /**
     * 最大容量 单位为kb
     */
    private Integer maxSize;

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }
}
