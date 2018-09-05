package com.xiaowei;

import com.xiaowei.commonupload.BaseUploadController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class AccountWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountWebApplication.class, args);
	}


	/**
	 * 配置文件上传通用服务
	 * @return
	 */
	@Bean
	public BaseUploadController baseUploadController(){
		return new BaseUploadController();
	}

	/**
	 * Jedis配置
	 * @return
	 */
	@Bean
	public ShardedJedisPool shardedJedisPool(){
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		//最大连接数, 默认8个
		jedisPoolConfig.setMaxTotal(1000);
		//最小空闲连接数, 默认0
		jedisPoolConfig.setMaxIdle(1);
		//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
		jedisPoolConfig.setMaxWaitMillis(10000);
		List<JedisShardInfo> jedisShardInfos = new ArrayList<>();
		jedisShardInfos.add(new JedisShardInfo(redisHost));

		return new ShardedJedisPool(jedisPoolConfig, jedisShardInfos);
	}

    @Value("${spring.redis.host}")
    private String redisHost;

}
