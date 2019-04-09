package com.yiran.base.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.yiran.base.core.data.BaseRespData;
import com.yiran.base.system.client.user.UserRestService;
import com.yiran.base.system.object.Role;
import com.yiran.base.system.object.User;

@Component
public class UserServiceTest {
	@Autowired
	private UserRestService userRestService;

	public BaseRespData save(String id) {
		User user = new User();
		user.setId(id);
		user.setAccount("admin");
		user.setName("admin");
		user.setPassword(new BCryptPasswordEncoder().encode("admin"));
		user.setEmail("lsb3524@163.com");

		return userRestService.save(user);
	}
	
	public BaseRespData update(String id, String... roleIds) {
		
		User user = new User();
		user.setId(id);
		user.setAccount("admin");
		user.setName("admin");
		user.setPassword(new BCryptPasswordEncoder().encode("admin"));
		user.setEmail("lsb3524@163.com");

		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		role.setId(roleIds[0]);

		roles.add(role);

		user.setRoles(roles);
		return userRestService.update(user);
	}
}