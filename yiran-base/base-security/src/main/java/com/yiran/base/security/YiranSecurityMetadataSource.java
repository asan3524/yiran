package com.yiran.base.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.yiran.base.system.client.role.RoleFuture;
import com.yiran.base.system.object.ResourceQo;
import com.yiran.base.system.object.RoleQo;

@Component
public class YiranSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private static final Logger logger = LoggerFactory.getLogger(YiranSecurityMetadataSource.class);

	@Autowired
	private RoleFuture roleFuture;

	private PathMatcher pathMatcher = new AntPathMatcher();

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		String url = ((FilterInvocation) object).getRequestUrl();

		List<RoleQo> roleQoList = loadRoles();

		// 有权限的角色列表
		Collection<ConfigAttribute> roles = new ArrayList<>();

		// 检查每个角色的资源，如果跟请求资源匹配，则加入角色列表。为后面权限检查提供依据
		if (roleQoList != null && roleQoList.size() > 0) {
			for (RoleQo roleQo : roleQoList) {// 循环角色列表
				List<ResourceQo> resourceQos = roleQo.getResources();
				if (resourceQos != null && resourceQos.size() > 0) {
					for (ResourceQo resourceQo : resourceQos) {// 循环资源列表
						if (resourceQo.getUrl() != null && pathMatcher.match(resourceQo.getUrl() + "/**", url)) {
							ConfigAttribute attribute = new SecurityConfig(roleQo.getName());
							roles.add(attribute);
							logger.debug("加入权限角色列表===角色资源：{}，角色名称：{}===", resourceQo.getUrl(), roleQo.getName());
							break;
						}
					}
				}
			}
		}
		return roles;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	private List<RoleQo> loadRoles() {
		String json = roleFuture.findList().join();
		@SuppressWarnings("serial")
		List<RoleQo> list = new Gson().fromJson(json, new TypeToken<List<RoleQo>>() {
		}.getType());
		return list;
	}

}
