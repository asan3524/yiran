package com.yiran.redis.support;

public interface DistributedCacheSupport<T> {
	void set(String key, T value);

	void set(String key, T value, Long expiry);

	void hashPut(String key, String hashKey, T hashValue);
	
	/**
	 * @param key
	 * @param hashKey
	 * @param hashValue
	 * @param expiry 单位mm，将导致hash所有value的过期时间都设置为expiry
	 */
	void hashPut(String key, String hashKey, T hashValue, Long expiry);

	T get(String key, Class<T> clazz);

	T hashGet(String key, String hashKey, Class<T> clazz);

	boolean delete(String key);
	
	boolean hashDelete(String key, String hashKey);
}
