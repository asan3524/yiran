package com.yiran.base.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableOAuth2Sso
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecuritySettings settings;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**").authorizeRequests().antMatchers(settings.getMatchers()).permitAll().anyRequest()
				.authenticated().and().csrf().requireCsrfProtectionMatcher(csrfSecurityRequestMatcher())
				.csrfTokenRepository(csrfTokenRepository()).and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
				.logout().and().exceptionHandling().accessDeniedPage(settings.getDeniedurl());
	}

	@Bean
	public YiranFilterSecurityInterceptor customFilter() throws Exception {
		YiranFilterSecurityInterceptor customFilter = new YiranFilterSecurityInterceptor();
		customFilter.setSecurityMetadataSource(securityMetadataSource());
		customFilter.setAccessDecisionManager(accessDecisionManager());
		customFilter.setAuthenticationManager(authenticationManagerBean());
		return customFilter;
	}

	@Bean
	public YiranAccessDecisionManager accessDecisionManager() {
		return new YiranAccessDecisionManager();
	}

	@Bean
	public YiranSecurityMetadataSource securityMetadataSource() {
		return new YiranSecurityMetadataSource();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	// 开放接口禁止防跨站伪造攻击
	private CsrfSecurityRequestMatcher csrfSecurityRequestMatcher() {
		CsrfSecurityRequestMatcher csrfSecurityRequestMatcher = new CsrfSecurityRequestMatcher();
		List<String> list = new ArrayList<String>();

		csrfSecurityRequestMatcher.setExecludeUrls(list);
		return csrfSecurityRequestMatcher;
	}

	private Filter csrfHeaderFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
				if (csrf != null) {
					Cookie cookie = new Cookie("XSRF-TOKEN", csrf.getToken());
					cookie.setPath("/");
					response.addCookie(cookie);
				}
				filterChain.doFilter(request, response);
			}
		};
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
}
