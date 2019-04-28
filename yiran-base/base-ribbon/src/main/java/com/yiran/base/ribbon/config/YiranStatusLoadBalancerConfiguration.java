package com.yiran.base.ribbon.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.sleuth.instrument.web.client.feign.YiranTraceLoadBalancerFeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.yiran.base.ribbon.balancer.YiranCachingSpringLoadBalancerFactory;
import com.yiran.base.ribbon.balancer.YiranLoadBalancerFeignClient;
import com.yiran.redis.cache.RedisCacheComponent;

import feign.Client;

public class YiranStatusLoadBalancerConfiguration {

	@Autowired
	private RedisCacheComponent cacheComponent;
	@Autowired
	private Registration registration;

	@Bean
	@Primary
	@ConditionalOnProperty(name = "spring.sleuth.feign.enabled", havingValue = "true", matchIfMissing = false)
	public Client traceFeignClient(SpringClientFactory clientFactory, LoadBalancedRetryFactory retryFactory,
			BeanFactory beanFactory) {
		return new YiranTraceLoadBalancerFeignClient(new Client.Default(null, null),
				new YiranCachingSpringLoadBalancerFactory(clientFactory), clientFactory, beanFactory, cacheComponent,
				registration.getUri().getAuthority());
	}

	@Bean
	@Primary
	@ConditionalOnProperty(name = "spring.sleuth.feign.enabled", havingValue = "false", matchIfMissing = true)
	public Client feignClient(SpringClientFactory clientFactory) {
		return new YiranLoadBalancerFeignClient(new Client.Default(null, null),
				new YiranCachingSpringLoadBalancerFactory(clientFactory), clientFactory, cacheComponent,
				registration.getUri().getAuthority());
	}
}
