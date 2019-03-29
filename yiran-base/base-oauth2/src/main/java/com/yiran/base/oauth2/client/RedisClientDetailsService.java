package com.yiran.base.oauth2.client;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import com.yiran.base.oauth2.builder.RedisClientDetailsServiceBuilder;
import com.yiran.redis.cache.RedisCacheComponent;

public class RedisClientDetailsService implements ClientDetailsService {

	private RedisCacheComponent<BaseClientDetails> cacheComponent;

	public RedisClientDetailsService(RedisCacheComponent<BaseClientDetails> cacheComponent) {
		// TODO Auto-generated constructor stub
		this.cacheComponent = cacheComponent;
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		// TODO Auto-generated method stub
		ClientDetails details = cacheComponent.hashGet(
				RedisClientDetailsServiceBuilder.YIRAN_BASE_OAUTH2_CENTER_CLIENTDETAILS_ID, clientId,
				BaseClientDetails.class);
		if (details == null) {
			throw new NoSuchClientException("No client with requested id: " + clientId);
		}
		return details;
	}
}