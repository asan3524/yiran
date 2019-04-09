package com.yiran.base.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceSecurityConfigurer extends ResourceServerConfigurerAdapter {

	@Autowired
	private SecuritySettings securitySettings;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http // 根据配置文件放行无需验证的url
				.authorizeRequests().antMatchers(securitySettings.getMatchers()).permitAll()
				// 其他所有请求都需要验证令牌
				.anyRequest().authenticated();
	}
}
