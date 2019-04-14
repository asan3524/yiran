package com.yiran.zipkin.provider.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiran.base.core.util.id.IdWorker;
import com.yiran.zipkin.provider.common.UserReq;
import com.yiran.zipkin.provider.common.UserRes;
import com.yiran.zipkin.provider.entity.User;
import com.yiran.zipkin.provider.service.UserService;

/**
 * 用于演示, 不依赖DAO
 * 
 * @author duyu
 *
 */
@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private IdWorker idWorker;

	private ConcurrentHashMap<Long, User> STORAGE = new ConcurrentHashMap<>();

	@Override
	public List<UserRes> getAllUsers() {
		log.info("查询所有用户");
		List<UserRes> list = STORAGE.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).map(e -> {
			UserRes userRes = new UserRes();
			BeanUtils.copyProperties(e.getValue(), userRes);
			e.getValue();
			return userRes;
		}).collect(Collectors.toList());
		if (null == list) {
			list = new ArrayList<>();
		}
		return list;
	}

	@Override
	public UserRes getUserById(Long userId) {
		log.info("查询指定用户 {}", userId);

		User user = STORAGE.get(userId);
		UserRes userDto = new UserRes();
		BeanUtils.copyProperties(user, userDto);
		return userDto;
	}

	@Override
	public UserRes saveUser(UserReq userRes) {
		log.info("添加用户");

		User user = new User();
		BeanUtils.copyProperties(userRes, user);
		Long id = idWorker.nextId();
		user.setId(id);
		user.setCreateTime(new Date());
		STORAGE.put(id, user);
		
		UserRes userDto = new UserRes();
		BeanUtils.copyProperties(user, userDto);
		return userDto;
	}

}
