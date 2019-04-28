package com.yiran.base.consumer.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.yiran.base.consumer.object.User;
import com.yiran.base.core.data.RespData;
import com.yiran.base.core.util.GsonUtil;

@Service
public class UserRestService {
	@Autowired
	private UserFeignClient userFeignClient;

	@SuppressWarnings("serial")
	public RespData<User> get(Long id) {
		String json = userFeignClient.findById(id);
		return GsonUtil.dateGson().fromJson(json, new TypeToken<RespData<User>>() {
		}.getType());
	}
}
