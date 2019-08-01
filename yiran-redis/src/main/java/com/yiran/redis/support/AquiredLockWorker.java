package com.yiran.redis.support;

import com.yiran.redis.exception.RedisLockException;

public interface AquiredLockWorker<T> {
	T invoke() throws RedisLockException;
}
