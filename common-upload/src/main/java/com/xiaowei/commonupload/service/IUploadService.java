package com.xiaowei.commonupload.service;


import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.commonupload.model.FileModel;
import com.xiaowei.commonupload.model.SchemeModel;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 上传服务 后期可以动态根据系统部署的情况，切换到FTP、本地、VFS上传等策略。
 */
public interface IUploadService {

    /**
     * 上传文件
     * @param fileModel  文件对象
     */
    public FileStore upload(FileModel fileModel);

    /**
     * 上传文件
     * @param fileModel  文件对象
     * @param schemeModel  约束对象
     */
    public FileStore upload(FileModel fileModel, SchemeModel schemeModel);

    /**
     * 获取上传文件流
     * @param
     * @return
     */
    public InputStream getFile(String filePath) throws FileNotFoundException;
}
