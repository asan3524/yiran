package com.yiran.base.resource.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class SecuritySettings {

	@Value("${security.oauth2.authorization.matchers:/swagger-ui.html, /webjars/**, /swagger-resources/**, /v2/api-docs, /static/**}")
	private String[] matchers;

	@Value("${security.oauth2.authorization.intercepts:/**}")
	private String[] intercepts;

	public String[] getMatchers() {
		return matchers;
	}

	public void setMatchers(String[] matchers) {
		this.matchers = matchers;
	}

	public String[] getIntercepts() {
		return intercepts;
	}

	public void setIntercepts(String[] intercepts) {
		this.intercepts = intercepts;
	}
}
