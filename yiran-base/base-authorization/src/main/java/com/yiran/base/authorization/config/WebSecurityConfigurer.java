package com.yiran.base.authorization.config;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import com.yiran.base.authorization.handler.YiranLoginSuccessHandler;
import com.yiran.base.authorization.service.LoginUserDetailsService;
import com.yiran.base.authorization.token.RedisTokenRepositoryImpl;

@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	private static final Logger Logger = LoggerFactory.getLogger(WebSecurityConfigurer.class);

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

		http // 配置登陆url, 登陆页面并无需验证
				.formLogin().loginPage(configProperties.getLoginurl()).permitAll().successHandler(loginSuccessHandler())
				// 根据配置文件放行无需验证的url
				.and().authorizeRequests().antMatchers(configProperties.getMatchers()).permitAll()
				// 其他所有请求都需要验证
				.anyRequest().authenticated()
				// 登出
				.and().logout().logoutUrl(configProperties.getLogouturl()).permitAll()
				// 错误页面
				.and().exceptionHandling().accessDeniedPage(configProperties.getDeniedurl())
				// session 策略
				// .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
				.and().sessionManagement().maximumSessions(1);
		if (null == redisTokenRepository) {
			http.rememberMe().tokenValiditySeconds(configProperties.getTokenvalidity())
					.tokenRepository(new InMemoryTokenRepositoryImpl());
		} else {
			Logger.info("=====================load redis for rememberMe save tokens====================");
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
