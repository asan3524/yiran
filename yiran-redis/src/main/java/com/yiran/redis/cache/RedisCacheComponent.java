package com.yiran.redis.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.GsonBuilder;
import com.yiran.redis.exception.RedisException;
import com.yiran.redis.support.DistributedCacheSupport;

@Component
public class RedisCacheComponent implements DistributedCacheSupport {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public <T> void set(String key, T value) {
		// TODO Auto-generated method stub
		String v = objToJson(key, value);
		BoundValueOperations<String, String> boundValueOperations = redisTemplate.boundValueOps(key);
		boundValueOperations.set(v);
	}

	@Override
	public <T> void set(String key, T value, Integer expiry) {
		// TODO Auto-generated method stub
		String v = objToJson(key, value);
		BoundValueOperations<String, String> boundValueOperations = redisTemplate.boundValueOps(key);
		boundValueOperations.set(v);
		//设置值之后再增加时间
		boundValueOperations.expire(expiry, TimeUnit.SECONDS);
	}

	@Override
	public boolean exist(String key) {
		// TODO Auto-generated method stub
		return redisTemplate.hasKey(key);
	}

	@Override
	public <T> void hashPut(String key, String hashKey, T hashValue) {
		// TODO Auto-generated method stub
		checkRedisKey(key);
		String v = objToJson(hashKey, hashValue);
		BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
		boundHashOperations.put(hashKey, v);
	}

	@Override
	public <T> void hashPut(String key, String hashKey, T hashValue, Integer expiry) {
		// TODO Auto-generated method stub
		checkRedisKey(key);
		String v = objToJson(hashKey, hashValue);
		BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
		boundHashOperations.put(hashKey, v);
		//设置值之后再增加时间， 整个hash生效
		boundHashOperations.expire(expiry, TimeUnit.SECONDS);
	}
	
	@Override
	public void hashPut(String key, Map<String, String> values) {
		// TODO Auto-generated method stub
		checkRedisKey(key);
		BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
		boundHashOperations.putAll(values);
	}
	
	@Override
	public void hashPut(String key, Map<String, String> values, Integer expiry) {
		// TODO Auto-generated method stub
		checkRedisKey(key);
		BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
		boundHashOperations.putAll(values);
		//设置值之后再增加时间， 整个hash生效
		boundHashOperations.expire(expiry, TimeUnit.SECONDS);
	}

	@Override
	public boolean hashExist(String key, String hashKey) {
		// TODO Auto-generated method stub
		if (redisTemplate.hasKey(key)) {
			BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
			return boundHashOperations.hasKey(hashKey);
		}
		return false;
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		// TODO Auto-generated method stub
		if (redisTemplate.hasKey(key)) {
			BoundValueOperations<String, String> boundValueOperations = redisTemplate.boundValueOps(key);
			return jsonToObj(boundValueOperations.get(), clazz);
		}
		return null;
	}

	@Override
	public <T> T hashGet(String key, String hashKey, Class<T> clazz) {
		// TODO Auto-generated method stub
		if (redisTemplate.hasKey(key)) {
			BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
			return jsonToObj(boundHashOperations.get(hashKey), clazz);
		} else {
			return null;
		}
	}

	@Override
	public <T> List<T> hashGet(String key, Class<T> clazz) {
		if (redisTemplate.hasKey(key)) {
			BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
			List<String> values = boundHashOperations.values();
			if (null != values && values.size() > 0) {
				List<T> result = new ArrayList<T>();
				for (String value : values) {
					T t = jsonToObj(value, clazz);
					if (null != t) {
						result.add(t);
					}
				}
				return result;
			}
		}
		return null;
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
		Long result = redisTemplate.delete(key);
		return null == result ? 0 : result;
	}

	@Override
	public long hashDelete(String key, Object... hashKey) {
		// TODO Auto-generated method stub
		if (redisTemplate.hasKey(key)) {
			BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
			Long result = boundHashOperations.delete(hashKey);
			return null == result ? 0 : result;
		} else {
			return 0;
		}
	}

	@Override
	public <T> void listSet(String key, Integer index, T listValue) {
		// TODO Auto-generated method stub
		checkRedisKey(key);
		String v = objToJson(key, listValue);
		BoundListOperations<String, String> boundListOperations = redisTemplate.boundListOps(key);
		boundListOperations.set(index, v);
	}

	@Override
	public <T> void listSet(String key, Integer index, T listValue, Integer expiry) {
		// TODO Auto-generated method stub
		checkRedisKey(key);
		String v = objToJson(key, listValue);
		BoundListOperations<String, String> boundListOperations = redisTemplate.boundListOps(key);
		boundListOperations.set(index, v);
		//设置值之后再增加时间， 整个list生效
		boundListOperations.expire(expiry, TimeUnit.SECONDS);
	}
	
	@Override
	public <T> void listPush(String key, T listValue) {
		// TODO Auto-generated method stub
		checkRedisKey(key);
		String v = objToJson(key, listValue);
		BoundListOperations<String, String> boundListOperations = redisTemplate.boundListOps(key);
		boundListOperations.leftPush(v);
	}

	@Override
	public <T> void listPush(String key, T listValue, Integer expiry) {
		// TODO Auto-generated method stub
		checkRedisKey(key);
		String v = objToJson(key, listValue);
		BoundListOperations<String, String> boundListOperations = redisTemplate.boundListOps(key);
		boundListOperations.leftPush(v);
		//设置值之后再增加时间， 整个list生效
		boundListOperations.expire(expiry, TimeUnit.SECONDS);
	}
	
	@Override
	public <T> T listLeftPop(String key, Class<T> clazz) {
		// TODO Auto-generated method stub
		if (redisTemplate.hasKey(key)) {
			BoundListOperations<String, String> boundListOperations = redisTemplate.boundListOps(key);
			return jsonToObj(boundListOperations.leftPop(), clazz);
		}
		return null;
	}

	@Override
	public <T> T listRightPop(String key, Class<T> clazz) {
		// TODO Auto-generated method stub
		if (redisTemplate.hasKey(key)) {
			BoundListOperations<String, String> boundListOperations = redisTemplate.boundListOps(key);
			return jsonToObj(boundListOperations.rightPop(), clazz);
		}
		return null;
	}

	@Override
	public <T> List<T> listGet(String key, Class<T> clazz) {
		// TODO Auto-generated method stub
		return listGet(key, 0, -1, clazz);
	}

	@Override
	public <T> List<T> listGet(String key, Integer fromIndex, Integer toIndex, Class<T> clazz) {
		// TODO Auto-generated method stub
		if (redisTemplate.hasKey(key)) {
			BoundListOperations<String, String> boundListOperations = redisTemplate.boundListOps(key);
			List<String> lists = boundListOperations.range(fromIndex, toIndex);
			if (null != lists && lists.size() > 0) {
				List<T> result = new ArrayList<T>();
				for (String value : lists) {
					T t = jsonToObj(value, clazz);
					if (null != t) {
						result.add(t);
					}
				}
				return result;
			}
		}
		return null;
	}

	@Override
	public <T> long listDelete(String key, T listValue) {
		// TODO Auto-generated method stub
		return listDelete(key, 1, listValue);
	}

	@Override
	public <T> long listDelete(String key, Integer index, T listValue) {
		// TODO Auto-generated method stub
		if (redisTemplate.hasKey(key)) {
			BoundListOperations<String, String> boundListOperations = redisTemplate.boundListOps(key);
			Long result = boundListOperations.remove(index, listValue);
			return null == result ? 0 : result;
		}
		return 0;
	}
	
	private <T> T jsonToObj(final String value, Class<T> clazz) {
		if (value == null) {
			return null;
		}
		return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(value, clazz);
	}

	private <T> String objToJson(final String key, final T value) {
		checkRedisKey(key);
		if (value == null) {
			throw new RedisException("value is null!");
		}
		String stringValue = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(value);
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