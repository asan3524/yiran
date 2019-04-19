package com.yiran.base.ribbon.balancer;

import java.util.Map;

import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer;
import org.springframework.util.ConcurrentReferenceHashMap;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;

public class YiranCachingSpringLoadBalancerFactory extends CachingSpringLoadBalancerFactory {

	private final SpringClientFactory factory;
	private LoadBalancedRetryFactory loadBalancedRetryFactory = null;

	private volatile Map<String, FeignLoadBalancer> cache = new ConcurrentReferenceHashMap<>();

	private Object key;

	public YiranCachingSpringLoadBalancerFactory(SpringClientFactory factory) {
		super(factory);
		this.factory = factory;
	}

	public YiranCachingSpringLoadBalancerFactory(SpringClientFactory factory,
			LoadBalancedRetryFactory loadBalancedRetryFactory) {
		super(factory, loadBalancedRetryFactory);
		this.factory = factory;
		this.loadBalancedRetryFactory = loadBalancedRetryFactory;
	}

	@Override
	public FeignLoadBalancer create(String clientName) {
		// TODO Auto-generated method stub
		FeignLoadBalancer client = this.cache.get(clientName);

		if (client != null) {
			if (client instanceof YiranFeignLoadBalancer) {
				if (((YiranFeignLoadBalancer) client).getKey().equals(key)) {
					return client;
				} else {
					((YiranFeignLoadBalancer) client).setKey(key);
					this.cache.put(clientName, client);
				}
			} else {
				return client;
			}
		}

		if (loadBalancedRetryFactory != null) {
			return super.create(clientName);
		}

		IClientConfig config = this.factory.getClientConfig(clientName);
		ILoadBalancer lb = this.factory.getLoadBalancer(clientName);
		ServerIntrospector serverIntrospector = this.factory.getInstance(clientName, ServerIntrospector.class);

		YiranFeignLoadBalancer yiranFeignLoadBalancer = new YiranFeignLoadBalancer(lb, config, serverIntrospector);
		yiranFeignLoadBalancer.setKey(key);

		this.cache.put(clientName, client);

		return (FeignLoadBalancer) yiranFeignLoadBalancer;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}
}
