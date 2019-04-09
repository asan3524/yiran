package com.yiran.base.authorization.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import com.yiran.base.authorization.client.RedisClientDetailsService;
import com.yiran.redis.cache.RedisCacheComponent;

/**
 * @author lishibang 自定义ClientDetailsServiceBuilder，借助redis存储ClientDetails信息
 */
@Component
@ConditionalOnBean(name = "redisProperties")
public class RedisClientDetailsServiceBuilder extends ClientDetailsServiceBuilder<RedisClientDetailsServiceBuilder> {

	public static final String YIRAN_BASE_OAUTH2_CENTER_CLIENTDETAILS_ID = "YIRAN_BASE_OAUTH2_CENTER_CLIENTDETAILS_ID_";

	@Autowired
	private RedisCacheComponent<BaseClientDetails> cacheComponent;

	@Override
	protected void addClient(String clientId, ClientDetails value) {
		cacheComponent.hashPut(YIRAN_BASE_OAUTH2_CENTER_CLIENTDETAILS_ID, clientId, (BaseClientDetails) value);
	}

	@Override
	protected ClientDetailsService performBuild() {
		RedisClientDetailsService clientDetailsService = new RedisClientDetailsService(cacheComponent);
		return clientDetailsService;
	}
}
