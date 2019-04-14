package com.yiran.zipkin.provider.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.yiran.zipkin.provider.common.UserReq;
import com.yiran.zipkin.provider.common.UserRes;

@FeignClient(name = "user-provider", fallbackFactory = ProductFeignClientFallback.class)
public interface ProviderFeignClient {
	
	@GetMapping("/user/list")
	List<UserRes> list();
	
	@GetMapping("/user/{id}")
	UserRes getUserById(@PathVariable("id") String id);

    @PostMapping("/user/save")
    UserRes save(@RequestBody UserReq user);
}
