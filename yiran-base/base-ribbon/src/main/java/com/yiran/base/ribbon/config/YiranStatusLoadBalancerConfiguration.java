package com.yiran.base.ribbon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;

import com.yiran.base.ribbon.balancer.YiranCachingSpringLoadBalancerFactory;
import com.yiran.base.ribbon.balancer.YiranLoadBalancerFeignClient;
import com.yiran.redis.cache.RedisCacheComponent;

import feign.Client;

public class YiranStatusLoadBalancerConfiguration {

	public static final String YIRAN_BASE_RIBBON_STATUS_TRANSACTIONA_ID = "YIRAN_BASE_RIBBON_STATUS_TRANSACTIONA_ID_";

	@Autowired
	private RedisCacheComponent cacheComponent;

	@Bean
	public Client feignClient(SpringClientFactory clientFactory) {
		return new YiranLoadBalancerFeignClient(new Client.Default(null, null),
				new YiranCachingSpringLoadBalancerFactory(clientFactory), clientFactory, cacheComponent);
	}
}
