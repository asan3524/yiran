package com.yiran.base.ribbon.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.yiran.base.core.data.Transaction;

import feign.RequestInterceptor;

public class YiranStatusInterceptorConfiguration {

	/**
	 * 有状态请求时，将transaction和validity通过头传递下去 需要在生产者的controller中使用
	 * 
	 * @RequestHeader(required = false) String
	 *                         transaction, @RequestHeader(required = false)
	 *                         Integer validity 获得事务ID和有效时间
	 * @return
	 */
	@Bean
	public RequestInterceptor requestInterceptor() {
		return requestTemplate -> {
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String value = request.getHeader(Transaction.YIRAN_BASE_HEADER_TRANSACTION);
				if (value != null) {
					requestTemplate.header(Transaction.YIRAN_BASE_HEADER_TRANSACTION, value);
				}
				String validity = request.getHeader(Transaction.YIRAN_BASE_HEADER_VALIDITY);
				if (validity != null) {
					requestTemplate.header(Transaction.YIRAN_BASE_HEADER_VALIDITY, validity);
				}
			}
		};
	}
}
