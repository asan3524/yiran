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
				YiranFeignLoadBalancer yflb = (YiranFeignLoadBalancer) client;
				yflb.setKey(key);
				return yflb;
			} else if (client instanceof YiranRetryableFeignLoadBalancer) {
				YiranRetryableFeignLoadBalancer yrflb = (YiranRetryableFeignLoadBalancer) client;
				yrflb.setKey(key);
				return yrflb;
			} else {
				return client;
			}
		}

		IClientConfig config = this.factory.getClientConfig(clientName);
		ILoadBalancer lb = this.factory.getLoadBalancer(clientName);
		ServerIntrospector serverIntrospector = this.factory.getInstance(clientName, ServerIntrospector.class);

		if (loadBalancedRetryFactory == null) {
			YiranFeignLoadBalancer yflb = new YiranFeignLoadBalancer(lb, config, serverIntrospector);

			this.cache.put(clientName, yflb);

			yflb.setKey(key);
			return yflb;
		} else {
			YiranRetryableFeignLoadBalancer yrflb = new YiranRetryableFeignLoadBalancer(lb, config, serverIntrospector,
					loadBalancedRetryFactory);
			this.cache.put(clientName, yrflb);

			yrflb.setKey(key);
			return yrflb;
		}
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}
}
