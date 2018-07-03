package com.xiaowei.commonupload;

import com.xiaowei.commonupload.service.IFileStoreService;
import com.xiaowei.commonupload.service.IUploadService;
import com.xiaowei.commonupload.service.impl.FTPUploadService;
import com.xiaowei.commonupload.service.impl.LocalUploadService;
import com.xiaowei.commonupload.service.impl.VFSUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadConfig {

    private static final Logger logger = LoggerFactory.getLogger(UploadConfig.class);

    @Autowired
    private UploadConfigBean uploadConfigBean;


    @Bean
    public IUploadService uploadService(@Autowired IFileStoreService fileStoreService){
        if("local".equalsIgnoreCase(uploadConfigBean.getType())){
            LocalUploadService localUploadService = new LocalUploadService();
            localUploadService.setDir(uploadConfigBean.getPath());
            localUploadService.setFileStoreService(fileStoreService);
            return localUploadService;
        }else if("ftp".equalsIgnoreCase(uploadConfigBean.getType())){
            //后期根据情况选择实现
            return new FTPUploadService();
        }else if("vfs".equalsIgnoreCase(uploadConfigBean.getType())){
            //后期根据情况选择实现
            return new VFSUploadService();
        }
        throw new RuntimeException("请设置上传类型!");
    }
}
