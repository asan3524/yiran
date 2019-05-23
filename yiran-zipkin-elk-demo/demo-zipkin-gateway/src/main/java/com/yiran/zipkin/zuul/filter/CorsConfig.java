package com.yiran.zipkin.zuul.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * 允许跨域配置
 */
@Component
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 设置允许cookie跨域
        config.setAllowedOrigins(Arrays.asList("*")); // 设置允许跨域的域名
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("*")); // 设置允许GET/PUT/POST/DELETE等
        config.setMaxAge(300l); //设置跨域生存时间, 单位:S

        source.registerCorsConfiguration("/**", config); //设置对所有接口路径生效
        return new CorsFilter(source);
    }
}