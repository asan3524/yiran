package com.yiran.zipkin.provider.client;

import java.util.List;

import org.springframework.stereotype.Component;

import com.yiran.zipkin.provider.common.UserReq;
import com.yiran.zipkin.provider.common.UserRes;

import feign.hystrix.FallbackFactory;

@Component
public class ProductFeignClientFallback implements FallbackFactory<ProviderFeignClient>  {

	@Override
	public ProviderFeignClient create(Throwable arg0) {
		return new ProviderFeignClient() {
			
			@Override
			public UserRes save(UserReq user) {
				return null;
			}
			
			@Override
			public List<UserRes> list() {
				return null;
			}
			
			@Override
			public UserRes getUserById(String id) {
				return null;
			}
		};
	}
	
}
