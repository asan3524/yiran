package com.yiran.base.system;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yiran.base.core.code.Code;
import com.yiran.base.core.data.BaseRespData;
import com.yiran.base.system.client.user.UserRestService;
import com.yiran.base.system.object.Role;
import com.yiran.base.system.object.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class UserTest {
	@Autowired
	private UserRestService userRestService;

	@Test
	public void save() {
		User user = new User();
		user.setAccount("lishibang");
		user.setName("lishibang");
		user.setPassword("123456");
		user.setEmail("lsb3524@163.com");

		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		role.setId(1l);
		role.setName("ADMIN");

		roles.add(role);

		user.setRoles(roles);
		BaseRespData result = userRestService.save(user);

		System.out.println(result);
		Assert.assertTrue(Code.SC_OK == result.getCode());
	}
}