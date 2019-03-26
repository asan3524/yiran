package com.yiran.redis.exception;

public class RedisException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RedisException() {
	}

	public RedisException(Throwable cause) {
		super(cause);
	}

	public RedisException(String message) {
		super(message);
	}

	public RedisException(String message, Throwable cause) {
		super(message, cause);
	}
}
