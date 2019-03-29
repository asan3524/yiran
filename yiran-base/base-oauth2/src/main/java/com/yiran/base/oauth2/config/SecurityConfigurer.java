package com.yiran.base.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.yiran.base.oauth2.builder.RedisClientDetailsServiceBuilder;

@Configuration
@EnableAuthorizationServer
public class SecurityConfigurer extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private JwtAccessTokenConverter jwtAccessTokenConverter;

	@Autowired(required = false)
	private RedisClientDetailsServiceBuilder redisClientDetailsServiceBuilder;

	@Value("${security.oauth2.client.client-id:eagleeye}")
	private String client_id;

	@Value("${security.oauth2.client.client-secret:{noop}thisissecret}")
	private String client_secret;

	@Value("${security.oauth2.client.scope:webclient,mobileclient}")
	private String[] scopes;

	@Value("${security.oauth2.client.authorized-grant-types:refresh_token,password,client_credentials}")
	private String[] grant_types;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// TODO Auto-generated method stub
		if (null == redisClientDetailsServiceBuilder) {
			clients.inMemory();
		} else {
			clients.setBuilder(redisClientDetailsServiceBuilder);
		}
		clients.and().withClient(client_id).secret(client_secret).authorizedGrantTypes(grant_types).scopes(scopes);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
				.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// TODO Auto-generated method stub
		endpoints.tokenStore(tokenStore).accessTokenConverter(jwtAccessTokenConverter)
				.authenticationManager(authenticationManager);
	}

}
