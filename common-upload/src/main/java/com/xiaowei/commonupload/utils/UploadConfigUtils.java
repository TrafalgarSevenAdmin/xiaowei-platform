package com.xiaowei.commonupload.utils;

import com.xiaowei.commonupload.UploadConfigBean;
import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.commonupload.service.IFileStoreService;
import com.xiaowei.core.context.ContextUtils;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class UploadConfigUtils implements CommandLineRunner {

    private static UploadConfigBean uploadConfigBean;
    private static IFileStoreService fileStoreService;

    public static String getPath() {

        return uploadConfigBean.getPath();
    }

    public static String getAccessUrlRoot() {

        return uploadConfigBean.getAccessUrlRoot();
    }

    public static String getType() {

        return uploadConfigBean.getType();
    }

    public static String[] getTags() {

        return uploadConfigBean.getTags();
    }

    public static List<FileStore> findByFileStoreId(String[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        return fileStoreService.findByIdIn(Arrays.stream(ids).collect(Collectors.toSet()));
    }

    public static List<FileStore> transIdsToPath(String idString) {
        if (StringUtils.isEmpty(idString)) {
            return null;
        }
        String[] ids = idString.split(";");
        final List<FileStore> fileStores = UploadConfigUtils.findByFileStoreId(ids);
        if (CollectionUtils.isEmpty(fileStores)) {
            return null;
        }
        fileStores.stream().forEach(fileStore -> {
            fileStore.setPath(UploadConfigUtils.getAccessUrlRoot() + fileStore.getPath());
        });
        return fileStores;
    }

    @Override
    public void run(String... args) throws Exception {
        if (UploadConfigUtils.uploadConfigBean == null) {
            UploadConfigUtils.uploadConfigBean = ContextUtils.getApplicationContext().getBean(UploadConfigBean.class);
            UploadConfigUtils.fileStoreService = ContextUtils.getApplicationContext().getBean(IFileStoreService.class);

        }
    }
}
