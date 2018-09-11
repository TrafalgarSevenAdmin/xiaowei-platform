package com.xiaowei.commonupload.utils;

import com.xiaowei.commonupload.UploadConfigBean;
import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.commonupload.service.IFileStoreService;
import com.xiaowei.core.context.ContextUtils;
import lombok.Data;
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

//    public static List<Map<String, String>> transIdsToPath(String idString) {
//        List<Map<String, String>> dataMap = null;
//        if (StringUtils.isEmpty(idString)) {
//            return dataMap;
//        }
//        String[] ids = idString.split(";");
//        final List<FileStore> fileStores = UploadConfigUtils.findByFileStoreId(ids);
//        if (CollectionUtils.isEmpty(fileStores)) {
//            return dataMap;
//        }
//        dataMap = new ArrayList<>();
//        for (FileStore fileStore : fileStores) {
//            Map<String, String> fileMap = new HashMap<>();
//            fileMap.put("id", fileStore.getId());
//            fileMap.put("path", uploadConfigBean.getAccessUrlRoot() + fileStore.getPath());
//            dataMap.add(fileMap);
//        }
//        return dataMap;
//    }

    @Override
    public void run(String... args) throws Exception {
        if (UploadConfigUtils.uploadConfigBean == null) {
            UploadConfigUtils.uploadConfigBean = ContextUtils.getApplicationContext().getBean(UploadConfigBean.class);
            UploadConfigUtils.fileStoreService = ContextUtils.getApplicationContext().getBean(IFileStoreService.class);

        }
    }
}
