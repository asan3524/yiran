package com.yiran.base.ribbon.balancer;

import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.openfeign.ribbon.RetryableFeignLoadBalancer;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand.Builder;

public class YiranRetryableFeignLoadBalancer extends RetryableFeignLoadBalancer {
	private Object key;

	public YiranRetryableFeignLoadBalancer(ILoadBalancer lb, IClientConfig clientConfig,
			ServerIntrospector serverIntrospector, LoadBalancedRetryFactory loadBalancedRetryFactory) {
		super(lb, clientConfig, serverIntrospector, loadBalancedRetryFactory);
	}

	@Override
	protected void customizeLoadBalancerCommandBuilder(RibbonRequest request, IClientConfig config,
			Builder<RibbonResponse> builder) {
		builder.withServerLocator(key);
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Object getKey() {
		return key;
	}
}
