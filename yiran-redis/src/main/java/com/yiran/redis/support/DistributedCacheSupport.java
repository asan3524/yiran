package com.yiran.redis.support;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DistributedCacheSupport {
	<T> void set(String key, T value);

	/**
	 * @param key
	 * @param value
	 * @param expiry 失效时间，单位秒，对当前key-value生效
	 */
	<T> void set(String key, T value, Integer expiry);

	boolean exist(String key);

	<T> void hashPut(String key, String hashKey, T hashValue);

	/**
	 * @param key
	 * @param hashKey
	 * @param hashValue
	 * @param expiry 失效时间，单位秒，对整个hash生效
	 */
	<T> void hashPut(String key, String hashKey, T hashValue, Integer expiry);
	
	void hashPut(String key, Map<String, String> values);

	/**
	 * @param key
	 * @param values
	 * @param expiry 失效时间，单位秒，对整个hash生效
	 */
	void hashPut(String key, Map<String, String> values, Integer expiry);
	
	boolean hashExist(String key, String hashKey);

	<T> T get(String key, Class<T> clazz);

	<T> T hashGet(String key, String hashKey, Class<T> clazz);

	<T> List<T> hashGet(String key, Class<T> clazz);

	boolean delete(String key);

	long delete(Collection<String> key);

	long hashDelete(String key, Object... hashKey);

	<T> void listSet(String key, Integer index, T listValue);

	/**
	 * @param key
	 * @param index
	 * @param listValue
	 * @param expiry 失效时间，单位秒，对整个list生效
	 */
	<T> void listSet(String key, Integer index, T listValue, Integer expiry);
	
	<T> void listPush(String key, T listValue);

	/**
	 * @param key
	 * @param listValue
	 * @param expiry 失效时间，单位秒，对整个list生效
	 */
	<T> void listPush(String key, T listValue, Integer expiry);
	
	<T> T listLeftPop(String key, Class<T> clazz);

	<T> T listRightPop(String key, Class<T> clazz);

	<T> List<T> listGet(String key, Class<T> clazz);

	<T> List<T> listGet(String key, Integer fromIndex, Integer toIndex, Class<T> clazz);
	
	<T> long listDelete(String key, T listValue);
	
	<T> long listDelete(String key, Integer index, T listValue);
}
