package com.yiran.base.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class SecuritySettings {

	@Value("${security.oauth2.authorization.matchers:/swagger-ui.html, /webjars/**, /swagger-resources/**, /v2/api-docs, /static/**}")
	private String[] matchers;

	public String[] getMatchers() {
		return matchers;
	}

	public void setMatchers(String[] matchers) {
		this.matchers = matchers;
	}
}
