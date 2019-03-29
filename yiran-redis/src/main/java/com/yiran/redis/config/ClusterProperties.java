package com.yiran.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author lishibang 
 * 名称必须为redisProperties，以便外部使用@ConditionalOnResource(resources
 *         = "redisProperties")验证redis是否加载
 */
@Configuration("redisProperties")
@ConditionalOnProperty(name = "spring.redis.cluster.nodes")
public class ClusterProperties {

	@Value("${spring.redis.cluster.nodes}")
	private String clusterNodes;

	@Value("${spring.redis.cluster.timeout:#{5}}")
	private Long timeout;

	@Value("${spring.redis.cluster.max-redirects:#{3}}")
	private int redirects;

	@Value("${spring.redis.cluster.password:#{null}}")
	private String password;

	public String getClusterNodes() {
		return clusterNodes;
	}

	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	public int getRedirects() {
		return redirects;
	}

	public void setRedirects(int redirects) {
		this.redirects = redirects;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "ClusterProperties [clusterNodes=" + clusterNodes + ", timeout=" + timeout + ", redirects=" + redirects
				+ ", password=" + password + "]";
	}
}
