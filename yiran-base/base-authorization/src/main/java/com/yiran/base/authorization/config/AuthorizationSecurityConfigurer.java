package com.yiran.base.authorization.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.yiran.base.authorization.builder.RedisClientDetailsServiceBuilder;
import com.yiran.base.authorization.token.RedisAuthenticationCodeServices;

@Configuration
@EnableAuthorizationServer
public class AuthorizationSecurityConfigurer extends AuthorizationServerConfigurerAdapter {

	private static final Logger Logger = LoggerFactory.getLogger(AuthorizationSecurityConfigurer.class);

	@Autowired
	private ConfigProperties configProperties;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private JwtAccessTokenConverter jwtAccessTokenConverter;

	@Autowired(required = false)
	private RedisClientDetailsServiceBuilder redisClientDetailsServiceBuilder;

	@Autowired(required = false)
	private RedisAuthenticationCodeServices redisAuthenticationCodeServices;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// TODO Auto-generated method stub
		if (null == redisClientDetailsServiceBuilder) {
			clients.inMemory();
		} else {
			Logger.info("===================== load redis for save ClientDetails ====================");
			clients.setBuilder(redisClientDetailsServiceBuilder);
		}
		clients.and().withClient(configProperties.getClient_id()).secret(configProperties.getClient_secret())
				.authorizedGrantTypes(configProperties.getGrant_types()).scopes(configProperties.getScopes());
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security
				// 开启/oauth/token_key验证端口无权限访问
				.tokenKeyAccess("permitAll()")
				// 开启/oauth/check_token验证端口认证权限访问
				.checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// TODO Auto-generated method stub
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();	
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new YiranTokenEnhancer(), jwtAccessTokenConverter));
		endpoints.tokenStore(tokenStore).tokenEnhancer(tokenEnhancerChain).authenticationManager(authenticationManager)
				.authorizationCodeServices(redisAuthenticationCodeServices);
	}

}
