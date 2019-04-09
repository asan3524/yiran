package com.yiran.base.consumer.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yiran.base.consumer.bean.UserInfo;

import feign.hystrix.FallbackFactory;

@Component
public class UserFeignClientFallBackFactory implements FallbackFactory<UserFeignClient> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserFeignClientFallBackFactory.class);
	
	@Override
	public UserFeignClient create(Throwable cause) {
		// TODO Auto-generated method stub
		return new UserFeignClient() {
			
			@Override
			public UserInfo findById(Long id) {
				// TODO Auto-generated method stub
				LOGGER.info("fallback; reason: ", cause);
				UserInfo ui = new UserInfo();
				ui.setAccount("Feign_fallback_account");
				ui.setId(-1l);
				ui.setUserAge(-1);
				ui.setUserName("Feign_fallback_name");
				return ui;
			}
		};
	}
}
