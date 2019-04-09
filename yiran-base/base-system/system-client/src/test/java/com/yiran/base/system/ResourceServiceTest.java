package com.yiran.base.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yiran.base.core.data.BaseRespData;
import com.yiran.base.system.client.resource.ResourceRestService;
import com.yiran.base.system.object.Resource;

@Component
public class ResourceServiceTest {
	@Autowired
	private ResourceRestService resourceRestService;

	public BaseRespData save(String id) {
		Resource resource = new Resource();
		resource.setId(id);
		resource.setMethod("GET");
		resource.setName("根据ID获取用户");
		resource.setUrl("/user/{id}");
		return resourceRestService.save(resource);
	}
}