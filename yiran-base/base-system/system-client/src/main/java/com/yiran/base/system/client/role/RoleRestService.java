package com.yiran.base.system.client.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.yiran.base.core.data.BaseRespData;
import com.yiran.base.core.data.PageResponseData;
import com.yiran.base.core.data.RespData;
import com.yiran.base.core.util.GsonUtil;
import com.yiran.base.system.object.Role;
import com.yiran.base.system.qo.RoleQo;

@Service
public class RoleRestService {
	@Autowired
	private RoleFeignClient roleFeignClient;

	public BaseRespData save(Role role) {
		String json = roleFeignClient.save(role);
		return GsonUtil.dateGson().fromJson(json, BaseRespData.class);
	}

	public BaseRespData delete(String id) {
		String json = roleFeignClient.delete(id);
		return GsonUtil.dateGson().fromJson(json, BaseRespData.class);
	}

	public BaseRespData update(Role role) {
		String json = roleFeignClient.update(role.getId(), role);
		return GsonUtil.dateGson().fromJson(json, BaseRespData.class);
	}

	@SuppressWarnings("serial")
	public RespData<Role> get(String id) {
		String json = roleFeignClient.findById(id);
		return GsonUtil.dateGson().fromJson(json, new TypeToken<RespData<Role>>() {
		}.getType());
	}

	@SuppressWarnings("serial")
	public PageResponseData<List<Role>> findPage(RoleQo roleQo, Integer index, Integer size) {
		String json = roleFeignClient.findPage(roleQo, index, size);
		return GsonUtil.dateGson().fromJson(json, new TypeToken<PageResponseData<List<Role>>>() {
		}.getType());
	}
}
