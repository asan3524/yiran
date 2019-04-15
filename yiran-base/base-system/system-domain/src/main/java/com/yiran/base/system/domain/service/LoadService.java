package com.yiran.base.system.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yiran.base.core.code.Code;
import com.yiran.base.core.data.BaseRespData;
import com.yiran.base.core.util.CommonUtils;
import com.yiran.base.core.util.CopyUtil;
import com.yiran.base.core.util.GsonUtil;
import com.yiran.base.system.constant.Constant;
import com.yiran.base.system.domain.dao.ResourceRepository;
import com.yiran.base.system.domain.dao.RoleRepository;
import com.yiran.base.system.domain.dao.UserRepository;
import com.yiran.base.system.domain.entity.ResourceInfo;
import com.yiran.base.system.domain.entity.RoleInfo;
import com.yiran.base.system.domain.entity.UserInfo;
import com.yiran.base.system.object.Resource;
import com.yiran.base.system.object.Role;
import com.yiran.base.system.object.User;
import com.yiran.redis.cache.RedisCacheComponent;

@Service
@Transactional
public class LoadService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RedisCacheComponent cacheComponent;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ResourceRepository resourceRepository;

	public BaseRespData loadUser() {
		BaseRespData brd = new BaseRespData();

		List<UserInfo> users = userRepository.findAll();

		if (CommonUtils.isNotNull(users)) {
			Map<String, String> umap = new HashMap<String, String>();

			Map<String, String> accountmap = new HashMap<String, String>();
			for (UserInfo ui : users) {
				User user = CopyUtil.copy(ui, User.class);
				if (null != user) {
					umap.put(user.getId(), GsonUtil.dateGson().toJson(user));
					accountmap.put(user.getAccount(), user.getId());
				}
			}
			cacheComponent.delete(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID);
			cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, umap);

			cacheComponent.delete(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ACCOUNT);
			cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ACCOUNT, accountmap);
		}

		brd.setCode(Code.SC_OK);
		brd.setMessage("load users sucess! users number is : " + users.size());
		return brd;
	}

	public BaseRespData loadRole() {
		BaseRespData brd = new BaseRespData();

		List<RoleInfo> roles = roleRepository.findAll();

		if (CommonUtils.isNotNull(roles)) {
			Map<String, String> umap = new HashMap<String, String>();
			for (RoleInfo ui : roles) {
				Role role = CopyUtil.copy(ui, Role.class);
				if (null != role) {
					umap.put(role.getId().toString(), GsonUtil.dateGson().toJson(role));
				}
			}
			cacheComponent.delete(Constant.YIRAN_BASE_SYSTEM_CENTER_ROLE_ID);
			cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_ROLE_ID, umap);
		}

		brd.setCode(Code.SC_OK);
		brd.setMessage("load roles sucess! roles number is : " + roles.size());
		return brd;
	}

	public BaseRespData loadResource() {
		BaseRespData brd = new BaseRespData();

		List<ResourceInfo> resources = resourceRepository.findAll();

		if (CommonUtils.isNotNull(resources)) {
			Map<String, String> umap = new HashMap<String, String>();
			for (ResourceInfo ui : resources) {
				Resource resource = CopyUtil.copy(ui, Resource.class);
				if (null != resource) {
					umap.put(resource.getId().toString(), GsonUtil.dateGson().toJson(resource));
				}
			}
			cacheComponent.delete(Constant.YIRAN_BASE_SYSTEM_CENTER_RESOURCE_ID);
			cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_RESOURCE_ID, umap);
		}

		brd.setCode(Code.SC_OK);
		brd.setMessage("load resources sucess! resources number is : " + resources.size());
		return brd;
	}
}
