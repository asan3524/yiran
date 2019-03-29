package com.yiran.redis.cache;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yiran.redis.exception.RedisException;
import com.yiran.redis.support.DistributedCacheSupport;

@Component
public class RedisCacheComponent<T> implements DistributedCacheSupport<T> {

	@Resource(name = "redisTemplate")
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public void set(String key, T value) {
		// TODO Auto-generated method stub
		String v = objToJson(key, value);
		BoundValueOperations<String, String> boundValueOperations = redisTemplate.boundValueOps(key);
		boundValueOperations.set(v);
	}

	@Override
	public void set(String key, T value, Integer expiry) {
		// TODO Auto-generated method stub
		String v = objToJson(key, value);
		BoundValueOperations<String, String> boundValueOperations = redisTemplate.boundValueOps(key);
		boundValueOperations.expire(expiry, TimeUnit.SECONDS);
		boundValueOperations.set(v, expiry);
	}

	@Override
	public void hashPut(String key, String hashKey, T hashValue) {
		// TODO Auto-generated method stub
		checkRedisKey(key);
		String v = objToJson(hashKey, hashValue);
		BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
		boundHashOperations.put(hashKey, v);
	}

	@Override
	public void hashPut(String key, String hashKey, T hashValue, Integer expiry) {
		// TODO Auto-generated method stub
		checkRedisKey(key);
		String v = objToJson(hashKey, hashValue);
		BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
		boundHashOperations.expire(expiry, TimeUnit.SECONDS);
		boundHashOperations.put(hashKey, v);
	}

	@Override
	public T get(String key, Class<T> clazz) {
		// TODO Auto-generated method stub
		if (redisTemplate.hasKey(key)) {
			BoundValueOperations<String, String> boundValueOperations = redisTemplate.boundValueOps(key);
			return jsonToObj(boundValueOperations.get(), clazz);
		}
		return null;
	}

	@Override
	public T hashGet(String key, String hashKey, Class<T> clazz) {
		// TODO Auto-generated method stub
		if (redisTemplate.hasKey(key)) {
			BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
			return jsonToObj(boundHashOperations.get(hashKey), clazz);
		} else {
			return null;
		}
	}

	@Override
	public boolean delete(String key) {
		// TODO Auto-generated method stub
		if (redisTemplate.hasKey(key)) {
			return redisTemplate.delete(key);
		} else {
			return false;
		}
	}

	@Override
	public long delete(Collection<String> key) {
		// TODO Auto-generated method stub
		return redisTemplate.delete(key);
	}

	@Override
	public boolean hashDelete(String key, Object... hashKey) {
		// TODO Auto-generated method stub
		if (redisTemplate.hasKey(key)) {
			BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
			return boundHashOperations.delete(hashKey) > 0;
		} else {
			return false;
		}
	}

	private T jsonToObj(final String value, Class<T> clazz) {
		if (value == null) {
			return null;
		}
		try {
			return JSON.parseObject(value, clazz);
		} catch (JSONException e) {
			return null;
		}
	}

	private String objToJson(final String key, final T value) {
		checkRedisKey(key);
		if (value == null) {
			throw new RedisException("value is null!");
		}
		String stringValue = null;
		try {
			stringValue = JSON.toJSONString(value, true);
		} catch (JSONException e) {
			throw new RedisException("value toJSONString exception!");
		}
		if (StringUtils.isEmpty(stringValue)) {
			throw new RedisException("value to json is null!");
		}
		return stringValue;
	}

	private void checkRedisKey(final String key) {
		if (StringUtils.isEmpty(key)) {
			throw new RedisException("key is null!");
		}
		if (key.length() > 1000000L) {
			throw new RedisException("length of key greater than 1M!");
		}
	}
}