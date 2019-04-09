package com.yiran.redis.support;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DistributedCacheSupport<T> {
	void set(String key, T value);

	void set(String key, T value, Integer expiry);

	boolean exist(String key);

	void hashPut(String key, String hashKey, T hashValue);

	/**
	 * @param key
	 * @param hashKey
	 * @param hashValue
	 * @param expiry
	 *            单位mm，将导致hash所有value的过期时间都设置为expiry
	 */
	void hashPut(String key, String hashKey, T hashValue, Integer expiry);

	void hashPut(String key, Map<String, String> values);

	void hashPut(String key, Map<String, String> values, Integer expiry);
	
	boolean hashExist(String key, String hashKey);

	T get(String key, Class<T> clazz);

	T hashGet(String key, String hashKey, Class<T> clazz);

	List<T> hashGet(String key, Class<T> clazz);
	
	boolean delete(String key);

	long delete(Collection<String> key);

	long hashDelete(String key, Object... hashKey);
}
