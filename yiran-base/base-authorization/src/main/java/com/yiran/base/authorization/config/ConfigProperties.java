package com.yiran.base.authorization.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class ConfigProperties {

	@Value("${security.oauth2.authorization.loginurl:/login}")
	private String loginurl;

	@Value("${security.oauth2.authorization.deniedurl:/deny}")
	private String deniedurl;

	@Value("${security.oauth2.authorization.logouturl:/logout}")
	private String logouturl;

	@Value("${security.oauth2.authorization.matchers:/images/**, /checkcode, /scripts/**, /styles/**}")
	private String[] matchers;

	@Value("${security.oauth2.authorization.tokenvalidity:#{300}}")
	private int tokenvalidity;

	@Value("${security.oauth2.client.client-id:eagleeye}")
	private String client_id;

	@Value("${security.oauth2.client.client-secret:{noop}thisissecret}")
	private String client_secret;

	@Value("${security.oauth2.client.scope:webclient,mobileclient}")
	private String[] scopes;

	@Value("${security.oauth2.client.authorized-grant-types:authorization_code,refresh_token,password,client_credentials}")
	private String[] grant_types;

	public String getLoginurl() {
		return loginurl;
	}

	public void setLoginurl(String loginurl) {
		this.loginurl = loginurl;
	}

	public String getDeniedurl() {
		return deniedurl;
	}

	public void setDeniedurl(String deniedurl) {
		this.deniedurl = deniedurl;
	}

	public String getLogouturl() {
		return logouturl;
	}

	public void setLogouturl(String logouturl) {
		this.logouturl = logouturl;
	}

	public String[] getMatchers() {
		return matchers;
	}

	public void setMatchers(String[] matchers) {
		this.matchers = matchers;
	}

	public int getTokenvalidity() {
		return tokenvalidity;
	}

	public void setTokenvalidity(int tokenvalidity) {
		this.tokenvalidity = tokenvalidity;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	public String[] getScopes() {
		return scopes;
	}

	public void setScopes(String[] scopes) {
		this.scopes = scopes;
	}

	public String[] getGrant_types() {
		return grant_types;
	}

	public void setGrant_types(String[] grant_types) {
		this.grant_types = grant_types;
	}
}
