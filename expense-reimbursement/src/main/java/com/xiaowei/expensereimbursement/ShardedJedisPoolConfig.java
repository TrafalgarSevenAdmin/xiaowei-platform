package com.xiaowei.expensereimbursement;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ShardedJedisPoolConfig {
    /**
     * Jedis配置
     * @return
     */
    @Bean
    @SuppressWarnings("all")
    public ShardedJedisPool shardedJedisPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(1000);
        //最小空闲连接数, 默认0
        jedisPoolConfig.setMaxIdle(1);
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(10000);
        List<JedisShardInfo> jedisShardInfos = new ArrayList<>();
        JedisShardInfo e = new JedisShardInfo(redisHost);
        e.setPassword(StringUtils.isNoneBlank(redisPassword)?redisPassword:null);
        jedisShardInfos.add(e);
        return new ShardedJedisPool(jedisPoolConfig, jedisShardInfos);
    }

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.password}")
    private String redisPassword;
}
