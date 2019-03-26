package com.yiran.redis.support;

import java.util.concurrent.TimeUnit;

public interface DistributedLockSupport {

	boolean isLocked(String key);

	boolean tryLock(String key);

	boolean tryLock(String key, long timeout, TimeUnit unit);

	void lock(String key);
}
