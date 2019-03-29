package com.yiran.base.system.client.user;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.yiran.base.system.object.UserQo;

@Component
public class UserFuture {
	@Autowired
	private UserRestService userRestService;

	@Async
	public CompletableFuture<String> findById(Long id) {
		return CompletableFuture.supplyAsync(() -> {
			return userRestService.findById(id);
		});
	}

	@Async
	public CompletableFuture<String> findByName(String name) {
		return CompletableFuture.supplyAsync(() -> {
			return userRestService.findByName(name);
		});
	}

	@Async
	public CompletableFuture<String> findList() {
		return CompletableFuture.supplyAsync(() -> {
			return userRestService.findList();
		});
	}

	@Async
	public CompletableFuture<String> findPage(UserQo userQo) {
		return CompletableFuture.supplyAsync(() -> {
			return userRestService.findPage(userQo);
		});
	}

	@Async
	public CompletableFuture<String> create(UserQo userQo) {
		return CompletableFuture.supplyAsync(() -> {
			String s = userRestService.create(userQo);
			System.out.println("result=================" + s);
			return s;
		});
	}

	@Async
	public CompletableFuture<String> update(UserQo userQo) {
		return CompletableFuture.supplyAsync(() -> {
			return userRestService.update(userQo);
		});
	}

	@Async
	public CompletableFuture<String> delete(Long id) {
		return CompletableFuture.supplyAsync(() -> {
			return userRestService.delete(id);
		});
	}
}
