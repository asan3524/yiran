package com.yiran.base.authorization.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/errors")
public class YiranErrorController implements ErrorController {

	@RequestMapping("/403")
	public String forbidden() {
		return "403";
	}

	@RequestMapping("/401")
	public String unauthorized() {
		return "401";
	}

	@Override
	public String getErrorPath() {
		return "/errors/403";
	}
}
