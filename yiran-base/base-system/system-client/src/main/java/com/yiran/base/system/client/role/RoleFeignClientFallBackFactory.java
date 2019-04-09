package com.yiran.base.system.client.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yiran.base.system.object.Role;
import com.yiran.base.system.qo.RoleQo;

import feign.hystrix.FallbackFactory;

@Component
public class RoleFeignClientFallBackFactory implements FallbackFactory<RoleFeignClient> {

	private static final Logger Logger = LoggerFactory.getLogger(RoleFeignClientFallBackFactory.class);

	@Override
	public RoleFeignClient create(Throwable cause) {
		// TODO Auto-generated method stub
		return new RoleFeignClient() {

			@Override
			public String findById(String id) {
				// TODO Auto-generated method stub
				Logger.info("fallback; reason: ", cause);
				
				return null;
			}

			@Override
			public String findPage(RoleQo roleQo, Integer index, Integer size) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String save(Role role) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String update(String id, Role role) {
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
