package com.yiran.base.system;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yiran.base.system.client.user.UserFuture;
import com.yiran.base.system.object.ResourceQo;
import com.yiran.base.system.object.RoleQo;
import com.yiran.base.system.object.UserQo;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class UserTest {
    @Autowired
    private UserFuture userFuture;
    @Test
    public void getKeys(){
    	UserQo userQo = new UserQo();
    	userQo.setId(1l);
    	userQo.setName("lishibang");
    	userQo.setPassword("123456");
    	userQo.setEmail("lsb3524@163.com");
    	
    	List<RoleQo> roles = new ArrayList<RoleQo>();
    	RoleQo role = new RoleQo();
    	role.setId(1l);
    	role.setName("ADMIN");
    	
    	List<ResourceQo> resources = new ArrayList<ResourceQo>();
    	ResourceQo resource = new ResourceQo();
    	resource.setId(1l);
    	resource.setName("user");
    	resource.setUrl("/user/*");
    	role.setResources(resources);
    	userQo.setRoles(roles);
    	CompletableFuture<String> result =userFuture.create(userQo);
    	
    	Assert.assertFalse("-1".equals(result));
    }
}