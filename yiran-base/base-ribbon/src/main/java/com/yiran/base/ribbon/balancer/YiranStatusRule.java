package com.yiran.base.ribbon.balancer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;

public class YiranStatusRule extends AbstractLoadBalancerRule {
	private static final Logger Logger = LoggerFactory.getLogger(YiranStatusRule.class);

	public Server choose(ILoadBalancer lb, Object key) {
		if (lb == null) {
			return null;
		}

		if (key == null) {
			// key为空时暂时使用轮询获取服务器，可以根据静态配置的路由数据选择
			Logger.info("key为空时暂时使用轮询获取服务器, 如果需要配置路由策略，请重写com.yiran.base.ribbon.balancer.YiranStatusRule");
			return new RoundRobinRule(lb).choose(null);
		} else {

			List<Server> allList = lb.getAllServers();
			Server server = null;

			for (Server temp : allList) {
				if (null != temp && temp.getId().equals(key)) {
					server = temp;
					Logger.info("key为" + key + "获取服务器:" + server);
					break;
				}
			}
			return server;
		}

	}

	@Override
	public Server choose(Object key) {
		return choose(getLoadBalancer(), key);
	}

	@Override
	public void initWithNiwsConfig(IClientConfig clientConfig) {
	}
}
