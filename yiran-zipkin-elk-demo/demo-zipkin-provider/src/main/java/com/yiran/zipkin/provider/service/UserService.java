package com.yiran.zipkin.provider.service;

import java.util.List;

import com.yiran.zipkin.provider.common.UserReq;
import com.yiran.zipkin.provider.common.UserRes;

public interface UserService {
	
	List<UserRes> getAllUsers();
	
	UserRes getUserById(Long userId);
	
	UserRes saveUser(UserReq user);
}
