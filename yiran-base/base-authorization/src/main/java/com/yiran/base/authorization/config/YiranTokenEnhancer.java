package com.yiran.base.authorization.config;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class YiranTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		// 添加自定义字段
		// Map<String, Object> additionalInfo = new HashMap<>();
		// additionalInfo.put("transaction", "6351192404132864");
		// ((DefaultOAuth2AccessToken)
		// accessToken).setAdditionalInformation(additionalInfo);
		return accessToken;
	}
}
