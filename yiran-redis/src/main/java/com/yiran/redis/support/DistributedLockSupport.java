package com.yiran.redis.support;

import java.util.concurrent.TimeUnit;

public interface DistributedLockSupport {

	void lock(String lockName);
	
	void lock(String lockName, long timeout, TimeUnit unit);
	
	void unlock(String lockName);
	
	boolean tryLock(String lockName);

	boolean tryLock(String lockName, long timeout, TimeUnit unit);
}
