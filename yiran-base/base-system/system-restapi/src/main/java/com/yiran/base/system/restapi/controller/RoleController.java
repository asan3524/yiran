package com.yiran.base.system.restapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.yiran.base.core.util.CommonUtils;
import com.yiran.base.core.util.CopyUtil;
import com.yiran.base.system.domain.entity.Resource;
import com.yiran.base.system.domain.entity.Role;
import com.yiran.base.system.domain.entity.User;
import com.yiran.base.system.domain.service.RoleService;
import com.yiran.base.system.domain.service.UserService;
import com.yiran.base.system.object.RoleQo;

@RestController
@RequestMapping("/role")
public class RoleController {
	private static Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;

	@RequestMapping("/{id}")
	public CompletableFuture<String> findById(@PathVariable Long id) {
		return CompletableFuture.supplyAsync(() -> roleService.findOne(id)).thenApply(role -> {
			return new Gson().toJson(role);
		});
	}

	@RequestMapping("/list")
	public CompletableFuture<String> findList() {
		return CompletableFuture.supplyAsync(() -> roleService.findAll()).thenApply(roles -> {
			return new Gson().toJson(roles);
		});
	}

	@RequestMapping(value = "/page")
	public CompletableFuture<String> findPage(Integer index, Integer size, String name) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				RoleQo roleQo = new RoleQo();

				if (CommonUtils.isNotNull(index)) {
					roleQo.setPage(index);
				}
				if (CommonUtils.isNotNull(size)) {
					roleQo.setSize(size);
				}
				if (CommonUtils.isNotNull(name)) {
					roleQo.setName(name);
				}

				Page<Role> roles = roleService.findAll(roleQo);

				Map<String, Object> page = new HashMap<>();
				page.put("content", roles.getContent());
				page.put("totalPages", roles.getTotalPages());
				page.put("totalelements", roles.getTotalElements());

				return new Gson().toJson(page);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public CompletableFuture<String> save(@RequestBody RoleQo roleQo) throws Exception {
		return CompletableFuture.supplyAsync(() -> {
			Role role = CopyUtil.copy(roleQo, Role.class);

			List<Resource> resourceList = CopyUtil.copyList(roleQo.getResources(), Resource.class);
			role.setResources(resourceList);

			roleService.save(role);

			logger.info("新增->ID=" + role.getId());
			return "1";
		});
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public CompletableFuture<String> update(@RequestBody RoleQo roleQo) throws Exception {
		return CompletableFuture.supplyAsync(() -> {
			Role role = CopyUtil.copy(roleQo, Role.class);

			List<Resource> resourceList = CopyUtil.copyList(roleQo.getResources(), Resource.class);
			role.setResources(resourceList);

			roleService.save(role);

			logger.info("修改->ID=" + role.getId());
			return "1";
		});
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public CompletableFuture<String> delete(@PathVariable Long id) throws Exception {
		return CompletableFuture.supplyAsync(() -> {
			// 让具有此角色的用户脱离关系
			List<User> userList = userService.findByRoleId(id);
			if (userList != null && userList.size() > 0) {
				for (User user : userList) {
					for (Role role : user.getRoles()) {
						if (role.getId().equals(id)) {
							user.getRoles().remove(role);
							userService.save(user);
							break;
						}
					}
				}
			}
			// 安全删除角色
			roleService.delete(id);
			logger.info("删除->ID=" + id);
			return "1";
		});
	}
}
