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
import com.yiran.base.system.domain.service.LoadService;
import com.yiran.base.system.domain.service.RoleService;
import com.yiran.base.system.object.Role;
import com.yiran.base.system.qo.RoleQo;
import com.yiran.base.web.controller.BaseController;

@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private LoadService loadService;

	@GetMapping(value = "/{id}")
	public Object findById(@PathVariable String id) {
		RespData<Role> result = roleService.get(id);
		return response(result);
	}

	@PostMapping(value = "/page")
	public Object findPage(@RequestBody RoleQo userQo,
			@PageableDefault(page = 0, size = 10, sort = "createTime") Pageable pageable, BindingResult errors) {

		PageRequestData<RoleQo> pageRequest = new PageRequestData<RoleQo>();

		pageRequest.setData(userQo);
		pageRequest.setPageable(pageable);
		PageResponseData<List<Role>> users = roleService.findAll(pageRequest);

		return response(users);
	}

	@PostMapping(value = "/save")
	public Object save(@RequestBody Role role, BindingResult errors) throws Exception {

		if (errors.hasErrors()) {
			String message = errors.getAllErrors().get(0).getDefaultMessage();
			return error(message);
		}

		if (CommonUtils.isNull(role.getId())) {
			role.setId(IdUtil.generateId().toString());
		}

		BaseRespData result = roleService.save(role);

		// 保存成功后缓存
		if (Code.SC_OK == result.getCode()) {
			CompletableFuture.supplyAsync(() -> roleService.load(role.getId()));
		}

		return response(result);
	}

	@PutMapping(value = "/update/{id}")
	public Object update(@PathVariable("id") Long id, @RequestBody Role role, BindingResult errors) throws Exception {

		if (errors.hasErrors()) {
			String message = errors.getAllErrors().get(0).getDefaultMessage();
			return error(message);
		}

		if (CommonUtils.isNull(id)) {
			return response(Code.SC_BAD_REQUEST, "id can not bu null");
		}

		BaseRespData result = roleService.update(role);

		// 保存成功后缓存
		if (Code.SC_OK == result.getCode()) {
			CompletableFuture.supplyAsync(() -> roleService.load(role.getId()));
			CompletableFuture.supplyAsync(() -> loadService.loadUser());
		}

		return response(result);
	}

	@DeleteMapping(value = "/delete/{id}")
	public Object delete(@PathVariable String id) throws Exception {
		BaseRespData result = roleService.delete(id);

		// 删除成功后刷新user缓存
		if (Code.SC_OK == result.getCode()) {
			CompletableFuture.supplyAsync(() -> loadService.loadUser());
		}
		return response(result);
	}
}
