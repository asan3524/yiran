package com.yiran.base.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yiran.base.core.data.BaseRespData;
import com.yiran.base.system.domain.service.LoadService;
import com.yiran.base.web.controller.BaseController;

@RestController
@RequestMapping("/load")
public class LoadController extends BaseController {

	@Autowired
	private LoadService loadService;

	@RequestMapping(value = "/user", method = { RequestMethod.GET, RequestMethod.POST })
	public Object loadUser() {
		BaseRespData result = loadService.loadUser();
		return response(result);
	}
}
