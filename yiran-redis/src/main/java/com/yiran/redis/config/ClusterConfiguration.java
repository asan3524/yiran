package com.yiran.redis.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnProperty(name = "spring.redis.cluster.nodes")
public class ClusterConfiguration {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ClusterProperties redisProperties;

	private RedisClusterConfiguration clusterConfiguration() {
		Map<String, Object> source = new HashMap<String, Object>();
		source.put("spring.redis.cluster.nodes", redisProperties.getClusterNodes());
		source.put("spring.redis.cluster.timeout", redisProperties.getTimeout());
		source.put("spring.redis.cluster.max-redirects", redisProperties.getRedirects());
		return new RedisClusterConfiguration(new MapPropertySource("ClusterConfiguration", source));
	}

	private JedisConnectionFactory connectionFactory() {
		logger.info("redis create connectionFactory load {}", redisProperties.toString());
		return new JedisConnectionFactory(clusterConfiguration());
	}

	@Bean(name = "redisTemplate")
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
		redisTemplate.setConnectionFactory(connectionFactory());
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		redisTemplate.setDefaultSerializer(stringRedisSerializer);
		redisTemplate.setEnableTransactionSupport(true);
		return redisTemplate;
	}
}
