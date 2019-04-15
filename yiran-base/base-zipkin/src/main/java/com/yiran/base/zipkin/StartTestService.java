package com.yiran.base.zipkin;

import com.yiran.base.zipkin.properties.LogstashProperties;

public class StartTestService {
	private LogstashProperties config;

	public StartTestService(LogstashProperties config) {
		super();
		this.config = config;
	}

	public void print() {
		System.out.println(config.toString());
	}
}
