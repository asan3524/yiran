package com.yiran.base.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yiran.base.ribbon.config.YiranStatusInterceptorConfiguration;
import com.yiran.base.ribbon.config.YiranStatusLoadBalancerConfiguration;

@FeignClient(name = "yiran-demo-provider", configuration = { YiranStatusLoadBalancerConfiguration.class,
		YiranStatusInterceptorConfiguration.class }, fallbackFactory = UserFeignClientFallBackFactory.class)
public interface UserFeignClient {

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public String findById(@PathVariable("id") Long id);
}
