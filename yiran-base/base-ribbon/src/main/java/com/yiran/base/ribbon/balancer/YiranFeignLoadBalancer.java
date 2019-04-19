package com.yiran.base.ribbon.balancer;

import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand.Builder;

public class YiranFeignLoadBalancer extends FeignLoadBalancer {

	private Object key;

	public YiranFeignLoadBalancer(ILoadBalancer lb, IClientConfig clientConfig, ServerIntrospector serverIntrospector) {
		super(lb, clientConfig, serverIntrospector);
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
