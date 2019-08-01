package com.yiran.redis.lock;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.yiran.redis.exception.RedisLockException;
import com.yiran.redis.support.AquiredLockWorker;
import com.yiran.redis.support.DistributedLockSupport;

@Component
@ConditionalOnProperty(name = "spring.redis.redisson", havingValue = "true")
public class RedisLockComponent implements DistributedLockSupport {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String YIRAN_DISTRIBUTED_LOCK = "YIRAN_DISTRIBUTED_LOCK_";

	@Autowired
	private RedissonClient redissonClient;

	@Override
	public void lock(String lockName) {
		// TODO Auto-generated method stub
		lock(lockName, 3, TimeUnit.SECONDS);
	}

	@Override
	public void lock(String lockName, long timeout, TimeUnit unit) {
		// TODO Auto-generated method stub
		RLock lock = redissonClient.getLock(YIRAN_DISTRIBUTED_LOCK + lockName);
		lock.lock(timeout, unit);
	}

	@Override
	public void unlock(String lockName) {
		// TODO Auto-generated method stub
		RLock lock = redissonClient.getLock(YIRAN_DISTRIBUTED_LOCK + lockName);
		lock.unlock();
	}

	@Override
	public boolean tryLock(String lockName) {
		// TODO Auto-generated method stub
		return tryLock(lockName, 2, TimeUnit.SECONDS);
	}

	@Override
	public boolean tryLock(String lockName, long timeout, TimeUnit unit) {
		// TODO Auto-generated method stub
		RLock lock = redissonClient.getLock(YIRAN_DISTRIBUTED_LOCK + lockName);
		try {
			return lock.tryLock(3, timeout, unit);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error("get lock {} error : {}", lockName, e.getMessage());
		}
		return false;
	}

	@Override
	public <T> T lock(String lockName, AquiredLockWorker<T> worker) throws RedisLockException {
		// TODO Auto-generated method stub
		return lock(lockName, worker, 3, TimeUnit.SECONDS);
	}

	@Override
	public <T> T lock(String lockName, AquiredLockWorker<T> worker, long timeout, TimeUnit unit)
			throws RedisLockException {
		// TODO Auto-generated method stub
		RLock lock = redissonClient.getLock(YIRAN_DISTRIBUTED_LOCK + lockName);
		try {
			boolean success = lock.tryLock(3, timeout, unit);

			if (success) {
				try {
					return worker.invoke();
				} finally {
					lock.unlock();
				}
			}
			throw new RedisLockException("get lock success but worker invoke exception");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error("get lock {} error : {}", lockName, e.getMessage());
			throw new RedisLockException(e);
		}
	}

}
