package com.xiaowei.commonupload.service.impl;


import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.commonupload.model.FileModel;
import com.xiaowei.commonupload.model.SchemeModel;
import com.xiaowei.commonupload.service.IUploadService;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * VFS文件上传
 */
public class VFSUploadService implements IUploadService {


    @Override
    public FileStore upload(FileModel fileModel) {
        return null;
    }

    @Override
    public FileStore upload(FileModel fileModel, SchemeModel schemeModel) {
        return null;
    }

    @Override
    public InputStream getFile(String filePath) throws FileNotFoundException {
        return null;
    }
}
