package com.yiran.zipkin.consumer.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiran.base.web.controller.BaseController;
import com.yiran.zipkin.provider.client.ProviderFeignClient;
import com.yiran.zipkin.provider.common.UserReq;
import com.yiran.zipkin.provider.common.UserRes;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/consumer")
public class ConsumerController extends BaseController  {
	private static final Logger log = LoggerFactory.getLogger(ConsumerController.class);
	
	@Autowired
	private ProviderFeignClient providerFeignClient;
	
	@ApiOperation(value = "查询用户列表")
	@GetMapping(value = "/getUserList")
	public List<UserRes> list() {
		log.info("consumer 测试获取用户列表");
		return providerFeignClient.list();
	}
	
	@ApiOperation(value = "查询指定用户")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", dataType = "String", paramType = "query", required = true, value = "用户ID")
	})
	@GetMapping(value = "/getUserById")
	public UserRes getUserById(@RequestParam(value = "id", required = true) String id) {
		log.info("consumer 测试获取指定用户 {}", id);
		return providerFeignClient.getUserById(id);
	}
	
	@ApiOperation(value = "添加用户")
	@PostMapping(value = "/save")
	public UserRes save(@Valid @RequestBody UserReq user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
            log.error("【添加用户】参数不正确, user={}", user);
            throw new RuntimeException(bindingResult.getFieldError().getDefaultMessage());
        }
		return providerFeignClient.save(user);
	}
}
