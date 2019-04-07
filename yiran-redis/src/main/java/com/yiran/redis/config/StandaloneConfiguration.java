package com.yiran.redis.config;

import javax.annotation.Resource;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnProperty(name = "spring.redis.host")
public class StandaloneConfiguration {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private StandaloneProperties standaloneProperties;

	private RedisStandaloneConfiguration standaloneConfiguration() {
		RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(
				standaloneProperties.getHost(), standaloneProperties.getPort());
		if (!StringUtils.isEmpty(standaloneProperties.getPassword())) {
			standaloneConfiguration.setPassword(RedisPassword.of(standaloneProperties.getPassword()));
		}
		return standaloneConfiguration;
	}

	private JedisConnectionFactory connectionFactory() {
		logger.info("redis create connectionFactory load {}", standaloneProperties.toString());
		return new JedisConnectionFactory(standaloneConfiguration());
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

	@Bean(name = "redissonClient")
	@ConditionalOnProperty(name = "spring.redis.redisson", havingValue = "true")
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer()
				.setAddress("redis://" + standaloneProperties.getHost() + ":" + standaloneProperties.getPort());
		return Redisson.create(config);
	}
}
