package com.yiran.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author lishibang 
 * 名称必须为redisProperties，以便外部使用
	@ConditionalOnBean(name = "redisProperties")验证redis是否加载
 */
@Configuration("redisProperties")
@ConditionalOnProperty(name = "spring.redis.host")
public class StandaloneProperties {

	@Value("${spring.redis.host}")
    private String host;
	
	@Value("${spring.redis.port:#{6379}}")
    private int port;
	
	@Value("${spring.redis.password:#{null}}")
    private String password;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "StandaloneProperties [host=" + host + ", port=" + port + ", password=" + password + "]";
	}
}
