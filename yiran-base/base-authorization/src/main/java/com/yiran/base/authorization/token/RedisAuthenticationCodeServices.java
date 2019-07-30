package com.yiran.base.authorization.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Component;

import com.yiran.base.core.util.ByteArrayUtils;
import com.yiran.redis.cache.RedisCacheComponent;

@Component
@ConditionalOnBean(name = "redisProperties")
public class RedisAuthenticationCodeServices extends RandomValueAuthorizationCodeServices {
	public static final String YIRAN_BASE_OAUTH2_CENTER_AUTHENTICATION_CODE = "YIRAN_BASE_OAUTH2_CENTER_AUTHENTICATION_CODE_";

	@Autowired
	private RedisCacheComponent cacheComponent;
//	private RedisTemplate<String, OAuth2Authentication> redisTemplateObject;

	@Override
	protected void store(String code, OAuth2Authentication authentication) {
		// TODO Auto-generated method stub
//		redisTemplateObject.boundHashOps(YIRAN_BASE_OAUTH2_CENTER_AUTHENTICATION_CODE).put(code, authentication);
		cacheComponent.hashPut(YIRAN_BASE_OAUTH2_CENTER_AUTHENTICATION_CODE, code,
				ByteArrayUtils.toHexString(SerializationUtils.serialize(authentication)));
	}

	@Override
	protected OAuth2Authentication remove(String code) {
		// TODO Auto-generated method stub
//		if (redisTemplateObject.hasKey(YIRAN_BASE_OAUTH2_CENTER_AUTHENTICATION_CODE)) {
//			BoundHashOperations<String, String, OAuth2Authentication> boundHashOperations = redisTemplateObject
//					.boundHashOps(YIRAN_BASE_OAUTH2_CENTER_AUTHENTICATION_CODE);
//			return boundHashOperations.get(code);
//		} else {
//			return null;
//		}
		return SerializationUtils.deserialize(ByteArrayUtils
				.toByteArray(cacheComponent.hashGet(YIRAN_BASE_OAUTH2_CENTER_AUTHENTICATION_CODE, code, String.class)));
	}
}
