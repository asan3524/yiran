package com.yiran.demo.provider.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Value("${security.oauth2.authorization.matchers}")
	private String[] matchers;

	@RefreshScope
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		// 页面放行
		System.out.println("=====================" + matchers[0]);
		http.authorizeRequests().antMatchers(matchers).permitAll().anyRequest().authenticated();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/user/**").hasRole("ADMIN").anyRequest().authenticated();
	}
}
