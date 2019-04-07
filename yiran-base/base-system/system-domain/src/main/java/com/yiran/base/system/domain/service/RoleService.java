package com.yiran.base.system.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yiran.base.core.code.Code;
import com.yiran.base.core.data.BaseRespData;
import com.yiran.base.core.data.PageRequestData;
import com.yiran.base.core.data.PageResponseData;
import com.yiran.base.core.data.RespData;
import com.yiran.base.core.util.CommonUtils;
import com.yiran.base.core.util.CopyUtil;
import com.yiran.base.system.constant.Constant;
import com.yiran.base.system.domain.dao.RoleRepository;
import com.yiran.base.system.domain.entity.ResourceInfo;
import com.yiran.base.system.domain.entity.RoleInfo;
import com.yiran.base.system.object.Resource;
import com.yiran.base.system.object.Role;
import com.yiran.base.system.qo.RoleQo;
import com.yiran.redis.cache.RedisCacheComponent;

@Service
@Transactional
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RedisCacheComponent<Role> roleCacheComponent;

	@Autowired
	private ResourceService resourceService;

	public BaseRespData save(Role role) {

		BaseRespData brd = new BaseRespData();

		role.setId(null);
		RoleInfo roleInfo = CopyUtil.copy(role, RoleInfo.class);

		List<Resource> resources = new ArrayList<Resource>();

		if (CommonUtils.isNotNull(role.getResources())) {

			List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
			for (Resource resource : role.getResources()) {
				if (null != resource.getId()) {
					RespData<Resource> rtemp = resourceService.get(resource.getId());

					Resource temp = rtemp.getData();
					if (null == temp) {
						brd.setCode(Code.SC_BAD_REQUEST);
						brd.setMessage("save role that resource id {" + resource.getId() + "} is not exist");
						return brd;
					}
					resources.add(temp);
					resourceInfos.add(CopyUtil.copy(temp, ResourceInfo.class));
				}
			}
			roleInfo.setResources(resourceInfos);
			;
		}

		roleRepository.save(roleInfo);
		// 保存成功后缓存
		if (CommonUtils.isNotNull(roleInfo.getId())) {

			Role roleChache = CopyUtil.copy(roleInfo, Role.class);
			roleChache.setResources(resources);

			roleCacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_ROLE_ID, roleChache.getId().toString(),
					roleChache);
		}

		brd.setCode(Code.SC_OK);
		return brd;
	}

	public BaseRespData delete(Long id) {

		BaseRespData brd = new BaseRespData();

		RoleInfo roleInfo = roleRepository.getOne(id);
		if (CommonUtils.isNotNull(roleInfo)) {
			// 删除缓存
			roleCacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_ROLE_ID, roleInfo.getId().toString());

			roleRepository.cleanUserToRole(id);

			roleRepository.deleteById(id);
			brd.setCode(Code.SC_OK);

		} else {
			brd.setCode(Code.SC_BAD_REQUEST);
			brd.setMessage("role by id {" + id + "} is not exist");
		}

		return brd;
	}

	public BaseRespData update(Role role) {
		BaseRespData brd = new BaseRespData();
		if (CommonUtils.isNull(role.getId())) {
			brd.setCode(Code.SC_BAD_REQUEST);
			brd.setMessage("update role id can not be null");
			return brd;
		}

		RoleInfo roleInfo = CopyUtil.copy(role, RoleInfo.class);

		List<Resource> resources = new ArrayList<Resource>();

		if (CommonUtils.isNotNull(role.getResources())) {

			List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
			for (Resource resource : role.getResources()) {
				if (null != resource.getId()) {
					RespData<Resource> rtemp = resourceService.get(resource.getId());

					Resource temp = rtemp.getData();
					if (null == temp) {
						brd.setCode(Code.SC_BAD_REQUEST);
						brd.setMessage("update role that resource id {" + resource.getId() + "} is not exist");
						return brd;
					}
					resources.add(temp);
					resourceInfos.add(CopyUtil.copy(temp, ResourceInfo.class));
				}
			}
			roleInfo.setResources(resourceInfos);
		}

		roleRepository.save(roleInfo);
		// 保存成功后缓存
		if (CommonUtils.isNotNull(roleInfo.getId())) {

			Role roleChache = CopyUtil.copy(roleInfo, Role.class);
			roleChache.setResources(resources);

			roleCacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_ROLE_ID, roleChache.getId().toString(),
					roleChache);
		}

		brd.setCode(Code.SC_OK);
		return brd;
	}

	public RespData<Role> get(Long id) {
		RespData<Role> rd = new RespData<Role>();

		Role role = roleCacheComponent.hashGet(Constant.YIRAN_BASE_SYSTEM_CENTER_ROLE_ID, id.toString(), Role.class);

		if (null != role) {
			rd.setCode(Code.SC_OK);
			rd.setData(role);
		} else {
			Optional<RoleInfo> temp = roleRepository.findById(id);
			if (temp.isPresent()) {
				role = CopyUtil.copy(temp.get(), Role.class);
				roleCacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_ROLE_ID, role.getId().toString(), role);

				rd.setCode(Code.SC_OK);
				rd.setData(role);
			} else {
				rd.setCode(Code.SC_BAD_REQUEST);
				rd.setMessage("role by id {" + id + "} is not exist");
			}
		}
		return rd;
	}

	public PageResponseData<List<Role>> findAll(PageRequestData<RoleQo> pageRequest) {

		Page<RoleInfo> result = roleRepository.findAll(new Specification<RoleInfo>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<RoleInfo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();

				if (CommonUtils.isNotNull(pageRequest.getData().getName())) {
					predicatesList
							.add(criteriaBuilder.like(root.get("name"), "%" + pageRequest.getData().getName() + "%"));
				}

				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));

				return query.getRestriction();
			}
		}, pageRequest.getPageable());

		PageResponseData<List<Role>> users = new PageResponseData<List<Role>>(pageRequest);

		users.setTotal(result.getTotalElements());
		users.setNumber(result.getNumberOfElements());
		users.setData(CopyUtil.copyList(result.getContent(), Role.class));

		users.setCode(Code.SC_OK);
		return users;
	}

}
