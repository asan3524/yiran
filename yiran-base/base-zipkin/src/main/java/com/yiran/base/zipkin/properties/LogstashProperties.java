package com.yiran.base.zipkin.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "logstash", ignoreUnknownFields = false)
public class LogstashProperties {

	private LogstashTcpProperties tcp;
	private LogstashUdpProperties udp;
	private Integer writeBufferSize;

	public LogstashTcpProperties getTcp() {
		return tcp;
	}

	public void setTcp(LogstashTcpProperties tcp) {
		this.tcp = tcp;
	}

	public Integer getWriteBufferSize() {
		return writeBufferSize;
	}

	public void setWriteBufferSize(Integer writeBufferSize) {
		this.writeBufferSize = writeBufferSize;
	}

	public LogstashUdpProperties getUdp() {
		return udp;
	}

	public void setUdp(LogstashUdpProperties udp) {
		this.udp = udp;
	}
}
