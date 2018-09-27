package com.xiaowei.commonupload.asyn;

import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.commonupload.utils.UploadConfigUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BatchCountDownLatch implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(BatchCountDownLatch.class);
    private List<FileStore> fileStoreList;
    private CountDownLatch countDownLatch;

    public BatchCountDownLatch(List<FileStore> fileStoreList, CountDownLatch countDownLatch) {
        this.fileStoreList = fileStoreList;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            for (FileStore fileStore : fileStoreList) {
                String path = fileStore.getPath();
                if (StringUtils.isEmpty(path)) {
                    continue;
                }
                String accessUrlRoot = UploadConfigUtils.getAccessUrlRoot();
                if (path.startsWith(accessUrlRoot)) {
                    path = path.replace(accessUrlRoot, "");
                }
                path = UploadConfigUtils.getPath() + "/" + path;
                File file = new File(path);
                // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
                if (file.exists() && file.isFile()) {
                    if (file.delete()) {
                        logger.info("删除单个文件" + path + "成功！");
                    } else {
                        logger.info("删除单个文件" + path + "失败！");
                    }
                } else {
                    logger.info("删除单个文件失败：" + path + "不存在！");
                }
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        countDownLatch.countDown();
    }
}
