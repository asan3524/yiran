package com.yiran.base.system.client.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.yiran.base.core.data.BaseRespData;
import com.yiran.base.core.data.PageResponseData;
import com.yiran.base.core.data.RespData;
import com.yiran.base.core.util.GsonUtil;
import com.yiran.base.system.object.Resource;
import com.yiran.base.system.qo.ResourceQo;

@Service
public class ResourceRestService {
	@Autowired
	private ResourceFeignClient resourceFeignClient;

	public BaseRespData save(Resource resource) {
		String json = resourceFeignClient.save(resource);
		return GsonUtil.dateGson().fromJson(json, BaseRespData.class);
	}

	public BaseRespData delete(String id) {
		String json = resourceFeignClient.delete(id);
		return GsonUtil.dateGson().fromJson(json, BaseRespData.class);
	}

	public BaseRespData update(Resource resource) {
		String json = resourceFeignClient.update(resource.getId(), resource);
		return GsonUtil.dateGson().fromJson(json, BaseRespData.class);
	}

	@SuppressWarnings("serial")
	public RespData<Resource> get(String id) {
		String json = resourceFeignClient.findById(id);
		return GsonUtil.dateGson().fromJson(json, new TypeToken<RespData<Resource>>() {
		}.getType());
	}

	@SuppressWarnings("serial")
	public PageResponseData<List<Resource>> findPage(ResourceQo resourceQo, Integer index, Integer size) {
		String json = resourceFeignClient.findPage(resourceQo, index, size);
		return GsonUtil.dateGson().fromJson(json, new TypeToken<PageResponseData<List<Resource>>>() {
		}.getType());
	}
}
