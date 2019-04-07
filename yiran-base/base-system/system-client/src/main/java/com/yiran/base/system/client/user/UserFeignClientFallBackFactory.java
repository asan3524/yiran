package com.yiran.base.system.client.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yiran.base.system.object.User;
import com.yiran.base.system.qo.UserQo;

import feign.hystrix.FallbackFactory;

@Component
public class UserFeignClientFallBackFactory implements FallbackFactory<UserFeignClient> {

	private static final Logger Logger = LoggerFactory.getLogger(UserFeignClientFallBackFactory.class);

	@Override
	public UserFeignClient create(Throwable cause) {
		// TODO Auto-generated method stub
		return new UserFeignClient() {

			@Override
			public String findById(Long id) {
				// TODO Auto-generated method stub
				Logger.info("fallback; reason: ", cause);
				return null;
			}

			@Override
			public String findByAccount(String account) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String findPage(UserQo userQo, Integer index, Integer size) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String save(User user) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String update(Long id, User user) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String delete(Long id) {
				// TODO Auto-generated method stub
				return null;
			}

		};
	}
}
