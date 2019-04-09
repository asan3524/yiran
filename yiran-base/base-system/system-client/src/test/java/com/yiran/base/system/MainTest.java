package com.yiran.base.system;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.yiran.base.core.code.Code;
import com.yiran.base.core.data.BaseRespData;
import com.yiran.base.core.util.IdUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class MainTest extends AbstractTestNGSpringContextTests {
	@Autowired
	private UserServiceTest userServiceTest;

	@Autowired
	private RoleServiceTest roleServiceTest;

	@Autowired
	private ResourceServiceTest resourceServiceTest;

	private String[] userIds;

	private String[] roleIds;

	private String[] resourceIds;
	
	@BeforeTest
	public void init() {
		userIds = new String[]{IdUtil.generateId().toString()};
		roleIds = new String[]{IdUtil.generateId().toString()};
		resourceIds = new String[]{IdUtil.generateId().toString()};
	}
	
	@Test(groups = "save")
	public void saveResource() {
		BaseRespData br = resourceServiceTest.save(resourceIds[0]);

		Assert.assertEquals(br.getCode(), Code.SC_OK);
	}

	@Test(groups = "save")
	public void saveRole() {
		BaseRespData br = roleServiceTest.save(roleIds[0]);

		Assert.assertEquals(br.getCode(), Code.SC_OK);
	}

	@Test(groups = "save")
	public void saveUser() {
		BaseRespData br = userServiceTest.save(userIds[0]);

		Assert.assertEquals(br.getCode(), Code.SC_OK);
	}

	@Test(dependsOnGroups = "save")
	public void updateRole() {
		BaseRespData br = roleServiceTest.update(roleIds[0], resourceIds);

		Assert.assertEquals(br.getCode(), Code.SC_OK);
	}

	@Test(dependsOnMethods = "updateRole")
	public void updateUser() {
		BaseRespData br = userServiceTest.update(userIds[0], roleIds);

		Assert.assertEquals(br.getCode(), Code.SC_OK);
	}
}