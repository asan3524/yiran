package com.yiran.base.system.client.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yiran.base.system.object.User;
import com.yiran.base.system.qo.UserQo;

@FeignClient(name = "yiran-system-restapi", fallbackFactory = UserFeignClientFallBackFactory.class)
public interface UserFeignClient {
	@RequestMapping(method = RequestMethod.GET, value = "/user/{id}")
	String findById(@RequestParam("id") Long id);

	@RequestMapping(method = RequestMethod.POST, value = "/user/account")
	String findByAccount(@RequestBody String account);

	@RequestMapping(method = RequestMethod.POST, value = "/user/page")
	String findPage(@RequestBody UserQo userQo, @RequestParam("index") Integer index,
			@RequestParam("size") Integer size);

	@RequestMapping(method = RequestMethod.POST, value = "/user/save")
	String save(@RequestBody User user);

	@RequestMapping(method = RequestMethod.PUT, value = "/user/update/{id}")
	String update(@RequestParam("id") Long id, @RequestBody User user);

	@RequestMapping(method = RequestMethod.DELETE, value = "/user/delete/{id}")
	String delete(@RequestParam("id") Long id);
}
