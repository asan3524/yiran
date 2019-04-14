package com.yiran.zipkin.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
@EnableCircuitBreaker
@ComponentScan(basePackages = "com.yiran.zipkin") 
@EnableHystrixDashboard
@EnableSwagger2
public class ProviderApplication {
	
	public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
