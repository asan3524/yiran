package com.yiran.redis.lock;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiran.redis.support.DistributedLockSupport;

public class RedisLockComponent implements DistributedLockSupport {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean isLocked(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryLock(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryLock(String key, long timeout, TimeUnit unit) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void lock(String key) {
		// TODO Auto-generated method stub

	}
}
