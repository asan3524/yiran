package com.yiran.base.system.client.role;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.yiran.base.system.object.RoleQo;

@Component
public class RoleFuture {
	@Autowired
	private RoleRestService roleRestService;

	@Async
	public CompletableFuture<String> findById(Long id) {
		return CompletableFuture.supplyAsync(() -> {
			return roleRestService.findById(id);
		});
	}

	@Async
	public CompletableFuture<String> findByName(String name) {
		return CompletableFuture.supplyAsync(() -> {
			return roleRestService.findByName(name);
		});
	}

	@Async
	public CompletableFuture<String> findList() {
		return CompletableFuture.supplyAsync(() -> {
			return roleRestService.findList();
		});
	}

	@Async
	public CompletableFuture<String> findPage(Integer index, Integer size, String name) {
		return CompletableFuture.supplyAsync(() -> {
			return roleRestService.findPage(index, size, name);
		});
	}

	@Async
	public CompletableFuture<String> create(RoleQo roleQo) {
		return CompletableFuture.supplyAsync(() -> {
			return roleRestService.create(roleQo);
		});
	}

	@Async
	public CompletableFuture<String> update(RoleQo roleQo) {
		return CompletableFuture.supplyAsync(() -> {
			return roleRestService.update(roleQo);
		});
	}

	@Async
	public CompletableFuture<String> delete(Long id) {
		return CompletableFuture.supplyAsync(() -> {
			return roleRestService.delete(id);
		});
	}
}
