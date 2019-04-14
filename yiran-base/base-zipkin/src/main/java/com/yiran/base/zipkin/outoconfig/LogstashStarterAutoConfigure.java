package com.yiran.base.zipkin.outoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yiran.base.zipkin.StartTestService;
import com.yiran.base.zipkin.properties.LogstashProperties;

@Configuration
@ConditionalOnClass(StartTestService.class)
@EnableConfigurationProperties(LogstashProperties.class)
public class LogstashStarterAutoConfigure {

	@Autowired
	private LogstashProperties properties;

	@Bean
	@ConditionalOnMissingBean
	StartTestService startTestService() {
		return new StartTestService(properties);
	}
}
