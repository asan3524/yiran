package com.yiran.base.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecuritySettings {

	@Value("${security.oauth2.authorization.loginurl:/logout}")
	private String logouturl;

	@Value("${security.oauth2.authorization.deniedurl:/deny}")
	private String deniedurl;

	@Value("${security.oauth2.authorization.matchers:/swagger-ui.html, /webjars/**, /swagger-resources/**, /v2/api-docs, /static/**}")
	private String[] matchers;

	public String getLogouturl() {
		return logouturl;
	}

	public void setLogouturl(String logouturl) {
		this.logouturl = logouturl;
	}

	public String getDeniedurl() {
		return deniedurl;
	}

	public void setDeniedurl(String deniedurl) {
		this.deniedurl = deniedurl;
	}

	public String[] getMatchers() {
		return matchers;
	}

	public void setMatchers(String[] matchers) {
		this.matchers = matchers;
	}
}
