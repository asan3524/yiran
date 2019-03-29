package com.yiran.base.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import com.yiran.base.oauth2.handler.LoginSuccessHandler;
import com.yiran.base.oauth2.service.LoginUserDetailsService;
import com.yiran.base.oauth2.token.RedisTokenRepositoryImpl;

@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Value("${security.oauth2.authorization.loginurl:/login}")
	private String loginurl;

	@Value("${security.oauth2.authorization.deniedurl:/deny}")
	private String deniedurl;

	@Value("${security.oauth2.authorization.matchers:/images/**, /checkcode, /scripts/**, /styles/**}")
	private String[] matchers;

	@Value("${security.oauth2.authorization.tokenvalidity:#{300}}")
	private int tokenvalidity;

	@Autowired
	private LoginUserDetailsService customUserDetailsService;

	@Autowired(required = false)
	private RedisTokenRepositoryImpl redisTokenRepository;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
		auth.eraseCredentials(false);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage(loginurl).permitAll().successHandler(loginSuccessHandler()).and().authorizeRequests()
				.antMatchers(matchers).permitAll().anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.NEVER).and().exceptionHandling()
				.accessDeniedPage(deniedurl);
		if (null == redisTokenRepository) {
			http.rememberMe().tokenValiditySeconds(tokenvalidity).tokenRepository(new InMemoryTokenRepositoryImpl());
		} else {
			http.rememberMe().tokenValiditySeconds(tokenvalidity)
					.tokenRepository(redisTokenRepository.validitySeconds(tokenvalidity));
		}
	}

	// @Bean
	// public BCryptPasswordEncoder passwordEncoder() {
	// return new BCryptPasswordEncoder();
	// }

	@Bean
	public LoginSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
