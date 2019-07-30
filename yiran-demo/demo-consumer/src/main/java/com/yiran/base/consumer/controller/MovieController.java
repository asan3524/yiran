package com.yiran.base.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiran.base.consumer.feign.UserRestService;
import com.yiran.base.consumer.object.User;
import com.yiran.base.core.data.RespData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/movie")
@Api(value = "/")
public class MovieController {

	@Autowired
	private UserRestService userRestService;

	@GetMapping("/{id}")
	@ApiOperation(value = "获取用户", notes = "根据用户ID，获取用户详情")
	public RespData<User> postId(@PathVariable Long id) {
		return userRestService.get(id);
	}
	
	@PostMapping("/get")
	@ApiOperation(value = "获取用户", notes = "根据用户ID，获取用户详情")
	public RespData<User> getId(@RequestBody Long id) {
		return userRestService.get(id);
	}
}
