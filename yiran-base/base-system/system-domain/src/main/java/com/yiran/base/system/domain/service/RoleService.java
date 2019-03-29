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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yiran.base.core.util.CommonUtils;
import com.yiran.base.system.domain.constant.Constant;
import com.yiran.base.system.domain.dao.RoleRepository;
import com.yiran.base.system.domain.entity.Role;
import com.yiran.base.system.object.RoleQo;
import com.yiran.redis.cache.RedisCacheComponent;

@Service
@Transactional
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private RedisCacheComponent<Role> cacheComponent;

	public void save(Role role) {
		// 删除缓存
		if (CommonUtils.isNotNull(role.getId())) {
			String key = role.getId().toString();
			cacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_ROLE_ID, key);// 删除原有缓存
		}
		roleRepository.save(role);
		// 保存缓存
		if (CommonUtils.isNotNull(role.getId())) {
			String key = role.getId().toString();
			cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_ROLE_ID, key, role);// 删除原有缓存
		}
	}

	public void delete(Long id) {
		// 删除缓存
		cacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_ROLE_ID, id.toString());

		roleRepository.deleteById(id);
	}

	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	public Role findOne(Long id) {
		Role role = cacheComponent.hashGet(Constant.YIRAN_BASE_SYSTEM_CENTER_ROLE_ID, id.toString(), Role.class);
		if (CommonUtils.isNull(role)) {
			Optional<Role> temp = roleRepository.findById(id);
			if (temp.isPresent()) {
				role = temp.get();
				cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_ROLE_ID, id.toString(), role, 12);
			}
		}
		return role;
	}

	public List<Role> findByResourceId(Long resourceId) {
		return roleRepository.findByResourceId(resourceId);
	}

	public Page<Role> findAll(RoleQo roleQo) {
		Sort sort = new Sort(Sort.Direction.DESC, "created");
		Pageable pageable = PageRequest.of(roleQo.getPage(), roleQo.getSize(), sort);

		return roleRepository.findAll(new Specification<Role>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();

				if (CommonUtils.isNotNull(roleQo.getName())) {
					predicatesList.add(criteriaBuilder.like(root.get("name"), "%" + roleQo.getName() + "%"));
				}
				if (CommonUtils.isNotNull(roleQo.getCreated())) {
					predicatesList.add(criteriaBuilder.greaterThan(root.get("created"), roleQo.getCreated()));
				}

				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));

				return query.getRestriction();
			}
		}, pageable);
	}

}
