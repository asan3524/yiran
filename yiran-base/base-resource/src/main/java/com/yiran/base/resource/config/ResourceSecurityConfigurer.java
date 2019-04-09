package com.yiran.base.resource.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import com.yiran.base.resource.filter.YiranAccessDecisionManager;
import com.yiran.base.resource.filter.YiranSecurityInterceptor;
import com.yiran.base.resource.filter.YiranSecurityMetadataSource;

@Configuration
@EnableResourceServer
public class ResourceSecurityConfigurer extends ResourceServerConfigurerAdapter {

	@Resource
	private SecuritySettings securitySettings;

	@Autowired
	private YiranSecurityMetadataSource yiranSecurityMetadataSource;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http // 根据配置文件放行无需验证的url
				.authorizeRequests().antMatchers(securitySettings.getMatchers()).permitAll()
				// 其他所有请求都需要验证令牌
				.anyRequest().authenticated();
	}

	@Bean
	@ConditionalOnBean(name = "yiranSecurityMetadataSource")
	public YiranSecurityInterceptor yiranSecurityInterceptor() throws Exception {
		YiranSecurityInterceptor yiranSecurityInterceptor = new YiranSecurityInterceptor();
		yiranSecurityInterceptor.setSecurityMetadataSource(yiranSecurityMetadataSource);
		yiranSecurityInterceptor.setAccessDecisionManager(new YiranAccessDecisionManager());
		return yiranSecurityInterceptor;
	}
}
