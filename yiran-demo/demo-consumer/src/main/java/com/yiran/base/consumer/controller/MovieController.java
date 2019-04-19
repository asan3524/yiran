package com.yiran.base.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiran.base.consumer.bean.UserInfo;
import com.yiran.base.consumer.feign.UserFeignClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/movie")
@Api(value = "/")
public class MovieController {

	@Autowired
	private UserFeignClient userFeignClient1;

	@GetMapping("/{id}")
	@ApiOperation(value = "获取用户", notes = "根据用户ID，获取用户详情")
	public UserInfo postId(@PathVariable Long id) {
		UserInfo user = userFeignClient1.findById(id);
		return user;
	}
}
