package com.yiran.base.sso.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.yiran.base.sso.config.SecuritySettings;
import com.yiran.base.system.constant.Constant;
import com.yiran.base.system.object.Resource;
import com.yiran.base.system.object.Role;
import com.yiran.redis.cache.RedisCacheComponent;

@Component(value = "yiranSecurityMetadataSource")
@ConditionalOnBean(name = "redisProperties")
public class YiranSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private static final Logger logger = LoggerFactory.getLogger(YiranSecurityMetadataSource.class);

	@Autowired
	private SecuritySettings configProperties;

	@Autowired
	private RedisCacheComponent cacheComponent;

	private PathMatcher pathMatcher = new AntPathMatcher();

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

		String url = ((FilterInvocation) object).getRequestUrl();
		String method = ((FilterInvocation) object).getHttpRequest().getMethod();

		int firstQuestionMarkIndex = url.indexOf("?");
		if (firstQuestionMarkIndex != -1) {
			url = url.substring(0, firstQuestionMarkIndex);
		}

		// 例外，返回null，不校验
		if (isMatcher(url)) {
			return null;
		}

		// 如果不在拦截列表中，返回null，不校验
		if (!isIntercept(url)) {
			return null;
		}

		// 此处调用远程微服务获取角色列表，可优化为从分布式缓存中获取
		List<Role> roleList = cacheComponent.hashGet(Constant.YIRAN_BASE_SYSTEM_CENTER_ROLE_ID, Role.class);

		// 有权限的角色列表
		Collection<ConfigAttribute> roles = new ArrayList<>();

		// 检查每个角色的资源，如果跟请求资源匹配，则加入角色列表。为后面权限检查提供依据
		if (roleList != null && roleList.size() > 0) {
			for (Role role : roleList) {
				List<Resource> resources = role.getResources();
				if (resources != null && resources.size() > 0) {
					for (Resource resource : resources) {
						// 如果设置了访问的method，则需要验证
						if (null != resource.getMethod()) {
							if (!method.equals(resource.getMethod())) {
								break;
							}
						}
						if (null != resource.getUrl() && pathMatcher.match(resource.getUrl(), url)) {
							ConfigAttribute attribute = new SecurityConfig(role.getCode());
							roles.add(attribute);
							logger.debug("角色:{}具备访问资源:{}的权限,加入可访问列表", role.getName(), resource.getUrl());
							break;
						}
					}
				}
			}
		}

		// 如果角色列表为空，不在例外，且处于拦截列表中，必须校验，且因该是无权限，所以生成随机角色让校验失败
		if (roles.isEmpty()) {
			ConfigAttribute attribute = new SecurityConfig(UUID.randomUUID().toString());
			roles.add(attribute);
			logger.debug("无任何角色具备访问资源:{}的权限,生成随机角色:{}加入可访问列表，以便进行控制", url, attribute.getAttribute());
		}
		return roles;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	private boolean isMatcher(String url) {
		String[] paths = configProperties.getMatchers();
		for (String path : paths) {
			if (pathMatcher.match(path, url)) {
				return true;
			}
		}
		return false;
	}

	private boolean isIntercept(String url) {
		String[] paths = configProperties.getIntercepts();
		for (String path : paths) {
			if (pathMatcher.match(path, url)) {
				return true;
			}
		}
		return false;
	}
}
