package com.yiran.base.authorization.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import com.yiran.base.authorization.handler.YiranLoginSuccessHandler;
import com.yiran.base.authorization.service.LoginUserDetailsService;
import com.yiran.base.authorization.token.RedisTokenRepositoryImpl;

@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Resource
	private ConfigProperties configProperties;

	@Autowired
	private LoginUserDetailsService loginUserDetailsService;

	@Autowired(required = false)
	private RedisTokenRepositoryImpl redisTokenRepository;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(loginUserDetailsService).passwordEncoder(passwordEncoder());
		auth.eraseCredentials(false);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http // 根据配置文件放行无需验证的url
				.authorizeRequests().antMatchers(configProperties.getMatchers()).permitAll().and().httpBasic()
				// 关闭csrf
				.and().csrf().disable()
				// 其他所有请求都需要验证
				.authorizeRequests().anyRequest().authenticated()
				// 配置登陆url, 登陆页面并无需验证
				.and().formLogin().loginPage(configProperties.getLoginurl()).permitAll()
				.successHandler(loginSuccessHandler())
				// 登出
				.and().logout().logoutUrl(configProperties.getLogouturl()).permitAll()
				// 错误页面
				.and().exceptionHandling().accessDeniedPage(configProperties.getDeniedurl()).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.NEVER);
		if (null == redisTokenRepository) {
			http.rememberMe().tokenValiditySeconds(configProperties.getTokenvalidity())
					.tokenRepository(new InMemoryTokenRepositoryImpl());
		} else {
			http.rememberMe().tokenValiditySeconds(configProperties.getTokenvalidity())
					.tokenRepository(redisTokenRepository.validitySeconds(configProperties.getTokenvalidity()));
		}
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public YiranLoginSuccessHandler loginSuccessHandler() {
		return new YiranLoginSuccessHandler();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
