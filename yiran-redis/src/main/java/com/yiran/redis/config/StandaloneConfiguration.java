package com.yiran.redis.config;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	private Jackson2JsonRedisSerializer<String> jackson2JsonRedisSerializer() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		Jackson2JsonRedisSerializer<String> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<String>(
				String.class);
		return jackson2JsonRedisSerializer;
	}

	@Bean(name = "redisTemplate")
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
		redisTemplate.setConnectionFactory(connectionFactory());
		redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer());
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.setEnableTransactionSupport(true);
		return redisTemplate;
	}
}
