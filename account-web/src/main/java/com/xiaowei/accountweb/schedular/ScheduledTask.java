package com.xiaowei.accountweb.schedular;

import com.xiaowei.commonupload.service.IFileStoreService;
import com.xiaowei.commonupload.utils.CheckFiledUtils;
import com.xiaowei.core.utils.MyListUtils;
import lombok.val;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ScheduledTask {

    @Autowired
    private IFileStoreService fileStoreService;
    @Autowired
    private CheckFiledUtils checkFiledUtils;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    /**
     * 每天晚上11点40分更新所有文件数据的检测时间并删除未符合检测的数据
     */
    @Scheduled(cron = "0 0 11 * * ? ")
    public void updateAllFileStoreCheckDate() {
        val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        log.info("检测文件数据 {}", dateFormat.format(currentDate));
        //1.查询出所有的文件的id
        final String[] names = checkFiledUtils.getNames();
        if (ArrayUtils.isEmpty(names)) {
            return;
        }
        Set<String> fileIds = new HashSet<>();
        for (String name : names) {
            final String[] split = name.split("\\.");//点之前为表名  点之后为字段名
            try {
                final List<String> checkIds = fileStoreService.getCheckIds(split[0], split[1]);
                for (String checkId : checkIds) {
                    if(StringUtils.isNotEmpty(checkId)){
                        fileIds.addAll(Arrays.stream(checkId.split(";")).collect(Collectors.toSet()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("表名" + split[0] + "或字段名" + split[1] + "有误!" + e.getMessage());
            }

        }
        //2.批量处理文件数据,更新检测时间,每一批为10000条数据
        final List<List<String>> list = MyListUtils.createList(fileIds.stream().collect(Collectors.toList()), 1000);
        list.stream().forEach(strings -> fileStoreService.updateCheckDate(strings, currentDate));
        //3.删除未符合检测的数据
        fileStoreService.deleteFileStoreByCheckDate(currentDate);
    }

}
