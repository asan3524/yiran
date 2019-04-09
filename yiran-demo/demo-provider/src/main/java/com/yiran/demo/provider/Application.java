package com.yiran.demo.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SpringBootApplication(scanBasePackages = { "com.yiran" })
@EnableDiscoveryClient
@EnableOAuth2Client
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
