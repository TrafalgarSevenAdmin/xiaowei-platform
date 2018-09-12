package com.xiaowei.account.authorization;

import com.xiaowei.account.consts.AccountConst;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouyang
 * @Date 2018-03-29 15:32:03
 * @Description
 * @Version 1.0
 */
@Component
public class RedisCacheSessionDao extends CachingSessionDAO {

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheSessionDao.class);

    //8小时的超时时间
    private int sessionTimeOut = 60*8;

    private final static String PREFIX = "USER_";

    //分组前缀
    private final static String REDIS_GROUP_PREFIX = "SYSTEM:USERS:LOGINED:";


    @Override
    protected Serializable doCreate(Session session) {
        String id = DigestUtils.md5Hex(PREFIX + UUID.randomUUID().toString());
        assignSessionId(session,id);
        redisTemplate.opsForValue().set(REDIS_GROUP_PREFIX+id, session,sessionTimeOut, TimeUnit.MINUTES);

        //添加当前到活跃用户中
        redisTemplate.opsForSet().add(AccountConst.ON_LINE_USER_KEY,id);
        return id;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        String id = sessionId.toString();
        Session session = (Session) redisTemplate.opsForValue().get(REDIS_GROUP_PREFIX+id);
        return session;
    }

    @Override
    protected void doUpdate(Session session) {
        redisTemplate.opsForValue().set(REDIS_GROUP_PREFIX + session.getId(), session, sessionTimeOut, TimeUnit.MINUTES);
    }

    @Override
    protected void doDelete(Session session) {
        redisTemplate.opsForValue().getOperations().delete(REDIS_GROUP_PREFIX+session.getId());

        //从活跃用户中删除当前退出的用户
        redisTemplate.opsForSet().remove(AccountConst.ON_LINE_USER_KEY,session.getId().toString());
    }
}
