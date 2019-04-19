package com.yiran.base.zipkin.properties;

public class LogstashTcpProperties {

    private String destination = "localhost:9250";

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
