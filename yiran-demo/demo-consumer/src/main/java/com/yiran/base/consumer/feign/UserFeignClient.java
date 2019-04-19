package com.yiran.base.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yiran.base.consumer.bean.UserInfo;

@FeignClient(name = "yiran-demo-provider", fallbackFactory = UserFeignClientFallBackFactory.class)
public interface UserFeignClient {

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public UserInfo findById(@PathVariable("id") Long id);
}
