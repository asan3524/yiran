package com.yiran.demo.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiran.base.core.code.Code;
import com.yiran.base.core.data.RespData;
import com.yiran.base.core.data.Transaction;
import com.yiran.base.core.util.CopyUtil;
import com.yiran.base.web.controller.BaseController;
import com.yiran.demo.provider.dao.UserRepository;
import com.yiran.demo.provider.entiry.UserInfo;
import com.yiran.demo.provider.object.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/user")
@Api(value = "/")
public class UserController extends BaseController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/{id}")
	@ApiOperation(value = "获取用户", notes = "根据用户ID，获取用户详情")
	public Object findById(@RequestHeader(required = false) String
			 transaction, @RequestHeader(required = false)
			 Integer validity, @PathVariable Long id) {
		UserInfo user = userRepository.getOne(id);
		RespData<User> response = new RespData<User>();
		
		response.setCode(Code.SC_OK);
		response.setData(CopyUtil.copy(user, User.class));
		response.setTransaction(new Transaction(transaction, validity));
		return response(response);
	}
}
