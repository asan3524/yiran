package com.yiran.base.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.yiran.base.system.constant.Constant;
import com.yiran.base.system.object.User;
import com.yiran.redis.cache.RedisCacheComponent;

@Component
@ConditionalOnBean(name = "redisProperties")
public class LoginUserDetailsService implements UserDetailsService {


	@Autowired
	private RedisCacheComponent cacheComponent;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = null;
		
		String id = cacheComponent.hashGet(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ACCOUNT, userName, String.class);
		
		if(null != id) {
			user = cacheComponent.hashGet(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, id, User.class);
		}
		
		if (user == null) {
			throw new UsernameNotFoundException("UserName " + userName + " not found");
		}
		return new SecurityUser(user);
	}
}
