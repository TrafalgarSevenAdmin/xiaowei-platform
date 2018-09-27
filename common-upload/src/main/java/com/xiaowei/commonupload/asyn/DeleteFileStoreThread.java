package com.xiaowei.commonupload.asyn;

import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.commonupload.repository.FileStoreRepository;
import com.xiaowei.core.context.ContextUtils;
import com.xiaowei.core.utils.MyListUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeleteFileStoreThread implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(DeleteFileStoreThread.class);
    private List<FileStore> fileStores;
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public DeleteFileStoreThread(List<FileStore> fileStores) {
        this.fileStores = fileStores;
    }

    @Override
    public void run() {
        if (CollectionUtils.isEmpty(fileStores)) {
            return;
        }
        FileStoreRepository fileStoreRepository = ContextUtils.getApplicationContext().getBean(FileStoreRepository.class);
        List<List<FileStore>> fileArrArr = MyListUtils.createList(fileStores, 100);
        try {
            //运行多线程,一个线程处理100个文件
            CountDownLatch countDownLatch = new CountDownLatch(fileArrArr.size());
            long startTime = System.currentTimeMillis();
            logger.info("开始删除文件，线程数:" + fileArrArr.size());
            for (List<FileStore> stores : fileArrArr) {
                cachedThreadPool.execute(new BatchCountDownLatch(stores,countDownLatch));
            }

            countDownLatch.await();
            fileStoreRepository.deleteAll(fileStores);
            logger.info("文件删除完毕，耗时ms:" + (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }

    }
}
