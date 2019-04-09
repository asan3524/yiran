package com.yiran.base.system.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiran.base.core.code.Code;
import com.yiran.base.core.data.BaseRespData;
import com.yiran.base.core.data.PageRequestData;
import com.yiran.base.core.data.PageResponseData;
import com.yiran.base.core.data.RespData;
import com.yiran.base.core.util.CommonUtils;
import com.yiran.base.core.util.IdUtil;
import com.yiran.base.system.domain.service.UserService;
import com.yiran.base.system.object.User;
import com.yiran.base.system.qo.UserQo;
import com.yiran.base.web.controller.BaseController;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
	// private static Logger logger =
	// LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping(value = "/{id}")
	public Object findById(@PathVariable String id) {
		RespData<User> result = userService.get(id);
		return response(result);
	}

	@PostMapping(value = "/account")
	public Object findByAccount(@RequestBody String account) {
		RespData<User> result = userService.findByAccount(account);
		return response(result);
	}

	@PostMapping(value = "/page")
	public Object findPage(@RequestBody UserQo userQo,
			@PageableDefault(page = 0, size = 10, sort = "createTime") Pageable pageable, BindingResult errors) {

		PageRequestData<UserQo> pageRequest = new PageRequestData<UserQo>();

		pageRequest.setData(userQo);
		pageRequest.setPageable(pageable);
		PageResponseData<List<User>> users = userService.findPage(pageRequest);

		return response(users);
	}

	@PostMapping(value = "/save")
	public Object save(@RequestBody User user, BindingResult errors) throws Exception {

		if (errors.hasErrors()) {
			String message = errors.getAllErrors().get(0).getDefaultMessage();
			return error(message);
		}

		if (CommonUtils.isNull(user.getAccount())) {
			return response(Code.SC_BAD_REQUEST, "account can not be null");
		}

		if (CommonUtils.isNull(user.getId())) {
			user.setId(IdUtil.generateId().toString());
		}

		BaseRespData result = userService.save(user);

		// 保存成功后缓存
		if (Code.SC_OK == result.getCode()) {
			CompletableFuture.supplyAsync(() -> userService.load(user.getId()));
		}

		return response(result);
	}

	@PutMapping(value = "/update/{id}")
	public Object update(@PathVariable("id") Long id, @RequestBody User user, BindingResult errors) throws Exception {

		if (errors.hasErrors()) {
			String message = errors.getAllErrors().get(0).getDefaultMessage();
			return error(message);
		}

		if (CommonUtils.isNull(id)) {
			return response(Code.SC_BAD_REQUEST, "id can not bu null");
		}

		BaseRespData result = userService.update(user);

		// 保存成功后缓存
		if (Code.SC_OK == result.getCode()) {
			CompletableFuture.supplyAsync(() -> userService.load(user.getId()));
		}

		return response(result);
	}

	@DeleteMapping(value = "/delete/{id}")
	public Object delete(@PathVariable String id) throws Exception {
		BaseRespData result = userService.delete(id);
		return response(result);
	}
}
