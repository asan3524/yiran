package com.yiran.base.system.client.resource;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yiran.base.system.object.Resource;
import com.yiran.base.system.qo.ResourceQo;

@FeignClient(name = "yiran-system-restapi", fallbackFactory = ResourceFeignClientFallBackFactory.class)
public interface ResourceFeignClient {
	@RequestMapping(method = RequestMethod.GET, value = "/resource/{id}")
	String findById(@RequestParam("id") String id);

	@RequestMapping(method = RequestMethod.POST, value = "/resource/page")
	String findPage(@RequestBody ResourceQo resourceQo, @RequestParam("index") Integer index,
			@RequestParam("size") Integer size);

	@RequestMapping(method = RequestMethod.POST, value = "/resource/save")
	String save(@RequestBody Resource resource);

	@RequestMapping(method = RequestMethod.PUT, value = "/resource/update/{id}")
	String update(@RequestParam("id") String id, @RequestBody Resource resource);

	@RequestMapping(method = RequestMethod.DELETE, value = "/resource/delete/{id}")
	String delete(@RequestParam("id") String id);
}
