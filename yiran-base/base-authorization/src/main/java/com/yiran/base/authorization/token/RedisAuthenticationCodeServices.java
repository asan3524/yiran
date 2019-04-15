package com.yiran.base.authorization.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Component;

import com.yiran.redis.cache.RedisCacheComponent;

@Component
@ConditionalOnBean(name = "redisProperties")
public class RedisAuthenticationCodeServices extends RandomValueAuthorizationCodeServices {
	public static final String YIRAN_BASE_OAUTH2_CENTER_AUTHENTICATION_CODE = "YIRAN_BASE_OAUTH2_CENTER_AUTHENTICATION_CODE_";

	@Autowired
	private RedisCacheComponent cacheComponent;

	@Override
	protected void store(String code, OAuth2Authentication authentication) {
		// TODO Auto-generated method stub
		cacheComponent.hashPut(YIRAN_BASE_OAUTH2_CENTER_AUTHENTICATION_CODE, code, authentication);
	}

	@Override
	protected OAuth2Authentication remove(String code) {
		// TODO Auto-generated method stub
		return cacheComponent.hashGet(YIRAN_BASE_OAUTH2_CENTER_AUTHENTICATION_CODE, code, OAuth2Authentication.class);
	}
}
