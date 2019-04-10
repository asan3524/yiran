package com.yiran.base.hystrix;

import java.util.concurrent.Callable;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public final class DelegatingYiranContextCallable<V> implements Callable<V> {

	private final Callable<V> delegate;
	private final RequestAttributes requestAttributes;

	public DelegatingYiranContextCallable(Callable<V> delegate, RequestAttributes requestAttributes) {
		this.delegate = delegate;
		this.requestAttributes = requestAttributes;
	}

	@Override
	public V call() throws Exception {
		try {
			// 待测试同时存在多个请求的时候是否有问题
			// 待测试与sleuth结合使用时是否有问题
			RequestContextHolder.setRequestAttributes(requestAttributes);
			return delegate.call();
		} finally {
			RequestContextHolder.resetRequestAttributes();
		}
	}

	public static <V> Callable<V> create(Callable<V> callable, RequestAttributes requestAttributes) {
		return new DelegatingYiranContextCallable<V>(callable, requestAttributes);
	}
}
