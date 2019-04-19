//package com.yiran.base.authorization.token;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
//import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2RefreshToken;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
//import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//
//import com.yiran.redis.cache.RedisCacheComponent;
//
//public class YiranRedisTokenStore implements TokenStore {
//
//	private static final String ACCESS = "access:";
//	private static final String AUTH_TO_ACCESS = "auth_to_access:";
//	private static final String AUTH = "auth:";
//	private static final String REFRESH_AUTH = "refresh_auth:";
//	private static final String ACCESS_TO_REFRESH = "access_to_refresh:";
//	private static final String REFRESH = "refresh:";
//	private static final String REFRESH_TO_ACCESS = "refresh_to_access:";
//	private static final String CLIENT_ID_TO_ACCESS = "client_id_to_access:";
//	private static final String UNAME_TO_ACCESS = "uname_to_access:";
//
//	private RedisCacheComponent redisCacheComponent;
//	private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
//
//	public YiranRedisTokenStore(RedisCacheComponent redisCacheComponent) {
//		this.redisCacheComponent = redisCacheComponent;
//	}
//
//	public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
//		this.authenticationKeyGenerator = authenticationKeyGenerator;
//	}
//
//	@Override
//	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
//		String key = authenticationKeyGenerator.extractKey(authentication);
//		OAuth2AccessToken accessToken = redisCacheComponent.get(AUTH_TO_ACCESS + key, YiranOAuth2AccessToken.class);
//		if (accessToken != null) {
//			OAuth2Authentication storedAuthentication = readAuthentication(accessToken.getValue());
//			if ((storedAuthentication == null
//					|| !key.equals(authenticationKeyGenerator.extractKey(storedAuthentication)))) {
//				// Keep the stores consistent (maybe the same user is
//				// represented by this authentication but the details have
//				// changed)
//				storeAccessToken(accessToken, authentication);
//			}
//
//		}
//		return accessToken;
//	}
//
//	@Override
//	public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
//		return readAuthentication(token.getValue());
//	}
//
//	@Override
//	public OAuth2Authentication readAuthentication(String token) {
//		OAuth2Authentication auth = redisCacheComponent.get(AUTH + token, OAuth2Authentication.class);
//		return auth;
//	}
//
//	@Override
//	public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
//		return readAuthenticationForRefreshToken(token.getValue());
//	}
//
//	public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
//		OAuth2Authentication auth = redisCacheComponent.get(REFRESH_AUTH + token, OAuth2Authentication.class);
//		return auth;
//	}
//
//	@Override
//	public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
//		if (token.getExpiration() != null) {
//			int seconds = token.getExpiresIn();
//			redisCacheComponent.set(ACCESS + token.getValue(), token, seconds);
//			redisCacheComponent.set(AUTH + token.getValue(), authentication, seconds);
//			redisCacheComponent.set(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication), token,
//					seconds);
//			if (!authentication.isClientOnly()) {
//				redisCacheComponent.listPush(UNAME_TO_ACCESS + getApprovalKey(authentication), token, seconds);
//			}
//			redisCacheComponent.listPush(CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId(), token,
//					seconds);
//		} else {
//			redisCacheComponent.set(ACCESS + token.getValue(), token);
//			redisCacheComponent.set(AUTH + token.getValue(), authentication);
//			redisCacheComponent.set(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication), token);
//			if (!authentication.isClientOnly()) {
//				redisCacheComponent.listPush(UNAME_TO_ACCESS + getApprovalKey(authentication), token);
//			}
//			redisCacheComponent.listPush(CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId(), token);
//		}
//		OAuth2RefreshToken refreshToken = token.getRefreshToken();
//		if (refreshToken != null && refreshToken.getValue() != null) {
//			Integer seconds = null;
//			if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
//				ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
//				Date expiration = expiringRefreshToken.getExpiration();
//				if (expiration != null) {
//					seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue();
//				}
//			}
//
//			if (null == seconds) {
//				redisCacheComponent.set(REFRESH_TO_ACCESS + token.getRefreshToken().getValue(), token.getValue());
//				redisCacheComponent.set(ACCESS_TO_REFRESH + token.getValue(), token.getRefreshToken().getValue());
//			} else {
//				redisCacheComponent.set(REFRESH_TO_ACCESS + token.getRefreshToken().getValue(), token.getValue(),
//						seconds);
//				redisCacheComponent.set(ACCESS_TO_REFRESH + token.getValue(), token.getRefreshToken().getValue(),
//						seconds);
//			}
//		}
//	}
//
//	private static String getApprovalKey(OAuth2Authentication authentication) {
//		String userName = authentication.getUserAuthentication() == null ? ""
//				: authentication.getUserAuthentication().getName();
//		return getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
//	}
//
//	private static String getApprovalKey(String clientId, String userName) {
//		return clientId + (userName == null ? "" : ":" + userName);
//	}
//
//	@Override
//	public void removeAccessToken(OAuth2AccessToken accessToken) {
//		removeAccessToken(accessToken.getValue());
//	}
//
//	@Override
//	public OAuth2AccessToken readAccessToken(String tokenValue) {
//		OAuth2AccessToken accessToken = redisCacheComponent.get(ACCESS + tokenValue, YiranOAuth2AccessToken.class);
//		return accessToken;
//	}
//
//	public void removeAccessToken(String tokenValue) {
//		OAuth2AccessToken token = redisCacheComponent.get(ACCESS + tokenValue, OAuth2AccessToken.class);
//		OAuth2Authentication auth = redisCacheComponent.get(AUTH + tokenValue, OAuth2Authentication.class);
//
//		redisCacheComponent.delete(ACCESS + tokenValue);
//		redisCacheComponent.delete(AUTH + tokenValue);
//		redisCacheComponent.delete(ACCESS_TO_REFRESH + tokenValue);
//
//		if (auth != null) {
//			String key = authenticationKeyGenerator.extractKey(auth);
//			redisCacheComponent.delete(AUTH_TO_ACCESS + key);
//			redisCacheComponent.listDelete(UNAME_TO_ACCESS + getApprovalKey(auth), token);
//			redisCacheComponent.listDelete(CLIENT_ID_TO_ACCESS + auth.getOAuth2Request().getClientId(), token);
//			redisCacheComponent.delete(ACCESS + key);
//		}
//	}
//
//	@Override
//	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
//
//		Integer seconds = null;
//
//		if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
//			ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
//			Date expiration = expiringRefreshToken.getExpiration();
//			if (expiration != null) {
//				seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue();
//			}
//		}
//
//		if (null == seconds) {
//			redisCacheComponent.set(REFRESH + refreshToken.getValue(), refreshToken);
//			redisCacheComponent.set(REFRESH_AUTH + refreshToken.getValue(), authentication);
//		} else {
//			redisCacheComponent.set(REFRESH + refreshToken.getValue(), refreshToken, seconds);
//			redisCacheComponent.set(REFRESH_AUTH + refreshToken.getValue(), authentication, seconds);
//		}
//	}
//
//	@Override
//	public OAuth2RefreshToken readRefreshToken(String tokenValue) {
//		OAuth2RefreshToken refreshToken = redisCacheComponent.get(REFRESH + tokenValue,
//				DefaultOAuth2RefreshToken.class);
//		return refreshToken;
//	}
//
//	@Override
//	public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
//		removeRefreshToken(refreshToken.getValue());
//	}
//
//	public void removeRefreshToken(String tokenValue) {
//		redisCacheComponent.delete(REFRESH + tokenValue);
//		redisCacheComponent.delete(REFRESH_AUTH + tokenValue);
//		redisCacheComponent.delete(REFRESH_TO_ACCESS + tokenValue);
//		redisCacheComponent.delete(ACCESS_TO_REFRESH + tokenValue);
//	}
//
//	@Override
//	public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
//		removeAccessTokenUsingRefreshToken(refreshToken.getValue());
//	}
//
//	private void removeAccessTokenUsingRefreshToken(String refreshToken) {
//		String tokenValue = redisCacheComponent.get(REFRESH_TO_ACCESS + refreshToken, String.class);
//		if (tokenValue != null) {
//			removeAccessToken(tokenValue);
//		}
//	}
//
//	@Override
//	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
//		List<YiranOAuth2AccessToken> accessTokens = redisCacheComponent
//				.listGet(UNAME_TO_ACCESS + getApprovalKey(clientId, userName), YiranOAuth2AccessToken.class);
//		return Collections.<OAuth2AccessToken>unmodifiableCollection(accessTokens);
//	}
//
//	@Override
//	public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
//		List<YiranOAuth2AccessToken> accessTokens = redisCacheComponent.listGet(CLIENT_ID_TO_ACCESS + clientId,
//				YiranOAuth2AccessToken.class);
//		return Collections.<OAuth2AccessToken>unmodifiableCollection(accessTokens);
//	}
//
//}
