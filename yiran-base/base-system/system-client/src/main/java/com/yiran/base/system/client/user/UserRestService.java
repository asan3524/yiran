package com.yiran.base.system.client.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.yiran.base.core.data.BaseRespData;
import com.yiran.base.core.data.PageResponseData;
import com.yiran.base.core.data.RespData;
import com.yiran.base.core.util.GsonUtil;
import com.yiran.base.system.object.User;
import com.yiran.base.system.qo.UserQo;

@Service
public class UserRestService {
	@Autowired
	private UserFeignClient userFeignClient;

	public BaseRespData save(User user) {
		String json = userFeignClient.save(user);
		return GsonUtil.dateGson().fromJson(json, BaseRespData.class);
	}

	public BaseRespData delete(String id) {
		String json = userFeignClient.delete(id);
		return GsonUtil.dateGson().fromJson(json, BaseRespData.class);
	}

	public BaseRespData update(User user) {
		String json = userFeignClient.update(user.getId(), user);
		return GsonUtil.dateGson().fromJson(json, BaseRespData.class);
	}

	@SuppressWarnings("serial")
	public RespData<User> get(String id) {
		String json = userFeignClient.findById(id);
		return GsonUtil.dateGson().fromJson(json, new TypeToken<RespData<User>>() {
		}.getType());
	}

	@SuppressWarnings("serial")
	public RespData<User> findByAccount(String account) {
		String json = userFeignClient.findByAccount(account);
		return GsonUtil.dateGson().fromJson(json, new TypeToken<RespData<User>>() {
		}.getType());
	}

	@SuppressWarnings("serial")
	public PageResponseData<List<User>> findPage(UserQo userQo, Integer index, Integer size) {
		String json = userFeignClient.findPage(userQo, index, size);
		return GsonUtil.dateGson().fromJson(json, new TypeToken<PageResponseData<List<User>>>() {
		}.getType());
	}
}
