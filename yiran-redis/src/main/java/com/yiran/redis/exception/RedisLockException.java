package com.yiran.redis.exception;

public class RedisLockException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RedisLockException() {
	}

	public RedisLockException(Throwable cause) {
		super(cause);
	}

	public RedisLockException(String message) {
		super(message);
	}

	public RedisLockException(String message, Throwable cause) {
		super(message, cause);
	}
}
