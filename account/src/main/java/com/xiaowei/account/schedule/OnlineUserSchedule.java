package com.xiaowei.account.schedule;


import com.xiaowei.account.consts.AccountConst;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author 在线用户定时器
 * @Date 2018-04-04 11:58:19
 * @Description 权限服务
 * @Version 1.0
 */
@Component
public class OnlineUserSchedule {

    private static final Logger logger = LoggerFactory.getLogger(OnlineUserSchedule.class);


    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @Scheduled(cron= "*/10 * * * * ?")
    public void run(){
        Set<String> onlineUserKeys = redisTemplate.opsForSet().members(AccountConst.ON_LINE_USER_KEY);
        List<String> removeKeys = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (String key : onlineUserKeys) {
            if(redisTemplate.getExpire(key) <= 0){
                removeKeys.add(key);
            }
        }
        if(!CollectionUtils.isEmpty(removeKeys)){
            Long removeInt = redisTemplate.opsForSet().remove(AccountConst.ON_LINE_USER_KEY, removeKeys.stream().map(v->AccountConst.ON_LINE_USER_KEY+v).toArray());
            logger.info("移除不在线用户:" + removeInt + "个");
        }

    }

}
