package com.yiran.base.sso.config;

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
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import com.yiran.base.sso.filter.CsrfSecurityRequestMatcher;
import com.yiran.base.sso.filter.YiranAccessDecisionManager;
import com.yiran.base.sso.filter.YiranSecurityInterceptor;
import com.yiran.base.sso.filter.YiranSecurityMetadataSource;

@Configuration
@EnableOAuth2Sso
public class SsoSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecuritySettings securitySettings;

	@Autowired
	private YiranSecurityMetadataSource yiranSecurityMetadataSource;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http // 根据配置文件放行无需验证的url
				.authorizeRequests().antMatchers(securitySettings.getMatchers()).permitAll()
				// 其他所有请求都需要验证令牌
				.anyRequest().authenticated()
				// 开启csrf过滤
				.and().csrf().requireCsrfProtectionMatcher(csrfSecurityRequestMatcher())
				.csrfTokenRepository(csrfTokenRepository()).and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
				// 登出
				.logout().logoutUrl(securitySettings.getLogouturl()).permitAll()
				// 错误
				.and().exceptionHandling().accessDeniedPage(securitySettings.getDeniedurl());
	}

	@Bean
	@ConditionalOnBean(name = "yiranSecurityMetadataSource")
	public YiranSecurityInterceptor yiranSecurityInterceptor() throws Exception {
		YiranSecurityInterceptor yiranSecurityInterceptor = new YiranSecurityInterceptor();
		yiranSecurityInterceptor.setSecurityMetadataSource(yiranSecurityMetadataSource);
		yiranSecurityInterceptor.setAccessDecisionManager(new YiranAccessDecisionManager());
		return yiranSecurityInterceptor;
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
