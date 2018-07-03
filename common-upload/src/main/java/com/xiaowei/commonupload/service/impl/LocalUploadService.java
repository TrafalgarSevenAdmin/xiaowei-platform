package com.xiaowei.commonupload.service.impl;

import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.commonupload.model.FileModel;
import com.xiaowei.commonupload.model.SchemeModel;
import com.xiaowei.commonupload.service.IFileStoreService;
import com.xiaowei.core.exception.BusinessException;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Date;

/**
 * 本地上传服务
 * @author mocker
 */
public class LocalUploadService extends BaseUploadService {

    private IFileStoreService fileStoreService;

    /**
     * 存储目录
     */
    private String dir;

    public void setFileStoreService(IFileStoreService fileStoreService) {
        this.fileStoreService = fileStoreService;
    }

    private File getAndCreateDir(String relativePath){
        if(StringUtils.isEmpty(dir)){
            throw new RuntimeException("请设置文件上传存储目录");
        }

        File file  = null;
        if(StringUtils.isEmpty(relativePath)){
            file = new File(dir);
        }else{
            file = new File(dir +File.separator +  relativePath);
        }
        if(!file.exists()){
            if(!file.mkdirs()){
                throw new RuntimeException("文件目录创建失败");
            }
        }
        return file;
    }

    @Override
    public FileStore upload(FileModel fileModel){
        return upload(fileModel,null);
    }

    @Override
    public FileStore upload(FileModel fileModel, SchemeModel schemeModel) {
        validate(fileModel,schemeModel);
        //从schemeModel判断文件上传约束 暂时不写

        File fileDir = getAndCreateDir(fileModel.getRelativePath());
        try {
            File file = new File(fileDir.getAbsolutePath(), fileModel.getFileName());
            if(file.exists()){
                if(!fileModel.isOverFile()){
                    throw new BusinessException("文件名已经存在!");
                }
            }

            FileStore fileStore = new FileStore();
            //需要在copyToByteArray之前运行 不然流关闭了
            fileStore.setSize((long) fileModel.getIn().available());

            try(FileOutputStream out = new FileOutputStream(file)){
                out.write(FileCopyUtils.copyToByteArray(fileModel.getIn()));
                out.flush();
            }

            fileStore.setPath(fileDir.getName() + File.separator + file.getName());
            fileStore.setOriginalFilename(fileModel.getOriginalFilename());
            fileStore.setCreatedTime(new Date());
            fileStore.setUserId(fileModel.getUserId());
            fileStoreService.save(fileStore);
            return fileStore;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getFile(String filePath) throws FileNotFoundException {
        File file = new File(getAndCreateDir(null),filePath);
        if(file.exists()){
            return new FileInputStream(file);
        }
        return null;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
