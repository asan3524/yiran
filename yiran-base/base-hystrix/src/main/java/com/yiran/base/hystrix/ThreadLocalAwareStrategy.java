package com.yiran.base.hystrix;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.web.context.request.RequestContextHolder;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;

public class ThreadLocalAwareStrategy extends HystrixConcurrencyStrategy {

	private HystrixConcurrencyStrategy existingConcurrencyStrategy;

	public ThreadLocalAwareStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy) {
		// TODO Auto-generated constructor stub
		this.existingConcurrencyStrategy = existingConcurrencyStrategy;
	}

	@Override
	public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
		// TODO Auto-generated method stub
		return null != existingConcurrencyStrategy ? existingConcurrencyStrategy.getBlockingQueue(maxQueueSize)
				: super.getBlockingQueue(maxQueueSize);
	}

	@Override
	public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
		// TODO Auto-generated method stub
		return null != existingConcurrencyStrategy ? existingConcurrencyStrategy.getRequestVariable(rv)
				: super.getRequestVariable(rv);
	}

	@Override
	public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixProperty<Integer> corePoolSize,
			HystrixProperty<Integer> maximumPoolSize, HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		// TODO Auto-generated method stub
		return null != existingConcurrencyStrategy
				? existingConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime,
						unit, workQueue)
				: super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override
	public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey arg0, HystrixThreadPoolProperties arg1) {
		// TODO Auto-generated method stub
		return null != existingConcurrencyStrategy ? existingConcurrencyStrategy.getThreadPool(arg0, arg1)
				: super.getThreadPool(arg0, arg1);
	}

	@Override
	public <T> Callable<T> wrapCallable(Callable<T> callable) {
		// TODO Auto-generated method stub
		return null != existingConcurrencyStrategy
				? existingConcurrencyStrategy.wrapCallable(
						DelegatingYiranContextCallable.create(callable, RequestContextHolder.getRequestAttributes()))
				: super.wrapCallable(
						DelegatingYiranContextCallable.create(callable, RequestContextHolder.getRequestAttributes()));
	}
}
