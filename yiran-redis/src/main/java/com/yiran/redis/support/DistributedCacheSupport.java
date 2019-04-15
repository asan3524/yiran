package com.yiran.redis.support;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DistributedCacheSupport {
	<T> void set(String key, T value);

	<T> void set(String key, T value, Integer expiry);

	boolean exist(String key);

	<T> void hashPut(String key, String hashKey, T hashValue);

	/**
	 * @param key
	 * @param hashKey
	 * @param hashValue
	 * @param expiry
	 *            单位mm，将导致hash所有value的过期时间都设置为expiry
	 */
	<T> void hashPut(String key, String hashKey, T hashValue, Integer expiry);

	void hashPut(String key, Map<String, String> values);

	void hashPut(String key, Map<String, String> values, Integer expiry);

	boolean hashExist(String key, String hashKey);

	<T> T get(String key, Class<T> clazz);

	<T> T hashGet(String key, String hashKey, Class<T> clazz);

	<T> List<T> hashGet(String key, Class<T> clazz);

	boolean delete(String key);

	long delete(Collection<String> key);

	long hashDelete(String key, Object... hashKey);

	<T> void listSet(String key, Integer index, T listValue);

	<T> void listSet(String key, Integer index, T listValue, Integer expiry);

	<T> void listPush(String key, T listValue);

	<T> void listPush(String key, T listValue, Integer expiry);

	<T> T listLeftPop(String key, Class<T> clazz);

	<T> T listRightPop(String key, Class<T> clazz);

	<T> List<T> listGet(String key, Class<T> clazz);

	<T> List<T> listGet(String key, Integer fromIndex, Integer toIndex, Class<T> clazz);
	
	<T> long listDelete(String key, T listValue);
	
	<T> long listDelete(String key, Integer index, T listValue);
}
