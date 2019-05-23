package com.yiran.zipkin.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@ComponentScan(basePackages = "com.yiran.zipkin") 
@EnableFeignClients(basePackages = {"com.yiran.zipkin.provider.client"})
@EnableHystrixDashboard
@EnableSwagger2
@EnableZuulProxy // 开启zuul
public class ConsumerApplication {
	
	public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
