package com.yiran.base.web.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiran.base.core.code.Code;

@RestController
@RequestMapping(value = "/errors")
public class YiranErrorController extends BaseController implements ErrorController {

	@RequestMapping("/403")
	public Object forbidden() {
		return response(Code.SC_FORBIDDEN, "forbidden");
	}

	@RequestMapping("/401")
	public Object unauthorized() {
		return response(Code.SC_UNAUTHORIZED, "Unauthorized");
	}

	@Override
	public String getErrorPath() {
		return "/errors/403";
	}
}
