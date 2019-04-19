package com.yiran.base.zipkin.outoconfig;

import com.yiran.base.zipkin.properties.LogstashProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LogstashProperties.class)
public class LogstashStarterAutoConfigure {

    @Autowired
    private LogstashProperties properties;
}
