package com.yiran.demo.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiran.demo.provider.dao.UserRepository;
import com.yiran.demo.provider.entiry.UserInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/user")
@Api(value = "/")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/{id}")
	@ApiOperation(value = "获取用户", notes = "根据用户ID，获取用户详情")
	public UserInfo findById(@PathVariable Long id) {
		UserInfo user = userRepository.getOne(id);
		return user;
	}
}
