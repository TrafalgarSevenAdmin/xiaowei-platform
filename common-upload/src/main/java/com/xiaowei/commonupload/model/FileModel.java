package com.xiaowei.commonupload.model;

import java.io.InputStream;

public class FileModel {

    /**
     * 文件流
     */
    private InputStream in;

    /**
     * 文件相对路径
     */
    private String relativePath;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 原始文件名称
     */
    private String originalFilename;

    private String userId;

    private boolean overFile;

    public InputStream getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isOverFile() {
        return overFile;
    }

    public void setOverFile(boolean overFile) {
        this.overFile = overFile;
    }
}
