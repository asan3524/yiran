package com.yiran.base.system.client.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yiran.base.system.object.Resource;
import com.yiran.base.system.qo.ResourceQo;

import feign.hystrix.FallbackFactory;

@Component
public class ResourceFeignClientFallBackFactory implements FallbackFactory<ResourceFeignClient> {

	private static final Logger Logger = LoggerFactory.getLogger(ResourceFeignClientFallBackFactory.class);

	@Override
	public ResourceFeignClient create(Throwable cause) {
		// TODO Auto-generated method stub
		return new ResourceFeignClient() {

			@Override
			public String findById(String id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String findPage(ResourceQo resourceQo, Integer index, Integer size) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String save(Resource resource) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String update(String id, Resource resource) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String delete(String id) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
