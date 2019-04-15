package com.yiran.zipkin.provider.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.yiran.base.web.controller.BaseController;
import com.yiran.zipkin.provider.common.UserReq;
import com.yiran.zipkin.provider.common.UserRes;
import com.yiran.zipkin.provider.service.UserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
//@DefaultProperties(defaultFallback = "defaultFallback")
public class UserController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;

	@ApiOperation(value = "查询用户列表")
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<UserRes> list() {
		return userService.getAllUsers();
	}
	
	@ApiOperation(value = "查询用户")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", dataType = "String", paramType = "path", required = true, value = "用户ID")
	})
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public UserRes getUserById(@PathVariable("id") String id) {
		return userService.getUserById(Long.parseLong(id));
	}
	
//	@HystrixCommand(
//	    commandKey = "save",
//	    commandProperties = {
//	            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
//	            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),                    
//	            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),       
//	            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), 
//	            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")     
//    })
	@ApiOperation(value = "添加用户")
	@PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public UserRes save(@Valid @RequestBody UserReq user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
            log.error("【添加用户】参数不正确, user={}", user);
            throw new RuntimeException(bindingResult.getFieldError().getDefaultMessage());
        }
		return userService.saveUser(user);
	}
	
	@SuppressWarnings("unused")
	private String defaultFallback() {
        return "系统繁忙中, 请稍后再试...";
    }

}
