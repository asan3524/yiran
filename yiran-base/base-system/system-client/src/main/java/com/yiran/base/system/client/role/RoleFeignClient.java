package com.yiran.base.system.client.role;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yiran.base.system.object.Role;
import com.yiran.base.system.qo.RoleQo;

@FeignClient(name = "yiran-system-restapi", fallbackFactory = RoleFeignClientFallBackFactory.class)
public interface RoleFeignClient {
	@RequestMapping(method = RequestMethod.GET, value = "/role/{id}")
	String findById(@RequestParam("id") String id);

	@RequestMapping(method = RequestMethod.POST, value = "/role/page")
	String findPage(@RequestBody RoleQo roleQo, @RequestParam("index") Integer index,
			@RequestParam("size") Integer size);

	@RequestMapping(method = RequestMethod.POST, value = "/role/save")
	String save(@RequestBody Role role);

	@RequestMapping(method = RequestMethod.PUT, value = "/role/update/{id}")
	String update(@RequestParam("id") String id, @RequestBody Role role);

	@RequestMapping(method = RequestMethod.DELETE, value = "/role/delete/{id}")
	String delete(@RequestParam("id") String id);
}
