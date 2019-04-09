package com.yiran.base.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yiran.base.core.data.BaseRespData;
import com.yiran.base.system.client.role.RoleRestService;
import com.yiran.base.system.object.Resource;
import com.yiran.base.system.object.Role;

@Component
public class RoleServiceTest {
	@Autowired
	private RoleRestService roleRestService;

	public BaseRespData save(String id) {
		
		Role role = new Role();
		role.setId(id);
		role.setCode("ADMIN");
		role.setName("管理员");

		return roleRestService.save(role);
	}
	
	public BaseRespData update(String id, String... resourceIds) {
		
		Role role = new Role();
		role.setId(id);
		role.setCode("ADMIN");
		role.setName("管理员");

		List<Resource> resources = new ArrayList<Resource>();
		Resource resource = new Resource();
		resource.setId(resourceIds[0]);

		resources.add(resource);
		role.setResources(resources);
		
		return roleRestService.update(role);
	}
}