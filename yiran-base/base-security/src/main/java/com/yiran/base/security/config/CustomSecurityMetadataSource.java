package com.yiran.base.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.yiran.base.core.util.CommonUtils;
import com.yiran.base.system.object.ResourceQo;
import com.yiran.base.system.object.RoleQo;
import com.yiran.redis.cache.RedisCacheComponent;

public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private static final Logger logger = LoggerFactory.getLogger(CustomSecurityMetadataSource.class);
	public static final String YIRAN_SYSTEM_CENTER_ROLES_ALL_ = "YIRAN_SYSTEM_CENTER_ROLES_ALL_";
	private PathMatcher pathMatcher = new AntPathMatcher();

	private RedisCacheComponent cacheComponent;

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	public CustomSecurityMetadataSource(RedisCacheComponent cacheComponent) {
		super();
		this.cacheComponent = cacheComponent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		String url = ((FilterInvocation) object).getRequestUrl();

		// 先从缓存中取角色列表
		Object objects = cacheComponent.get(YIRAN_SYSTEM_CENTER_ROLES_ALL_, "LIST");
		List<RoleQo> roleQoList = null;
		if (CommonUtils.isNull(objects)) {
			logger.info("从缓存中获取角色列表为空，请检查redis或者刷新缓存");
		} else {
			roleQoList = (List<RoleQo>) objects;
		}

		Collection<ConfigAttribute> roles = new ArrayList<>();// 有权限的角色列表

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
}
