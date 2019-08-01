package com.yiran.redis.support;

import java.util.concurrent.TimeUnit;

import com.yiran.redis.exception.RedisLockException;

public interface DistributedLockSupport {

	void lock(String lockName);

	void lock(String lockName, long timeout, TimeUnit unit);

	void unlock(String lockName);

	boolean tryLock(String lockName);

	boolean tryLock(String lockName, long timeout, TimeUnit unit);

	<T> T lock(String lockName, AquiredLockWorker<T> worker) throws RedisLockException;

	<T> T lock(String lockName, AquiredLockWorker<T> worker, long timeout, TimeUnit unit) throws RedisLockException;
}
