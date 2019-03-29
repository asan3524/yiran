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
import com.yiran.base.system.domain.dao.UserRepository;
import com.yiran.base.system.domain.entity.User;
import com.yiran.base.system.object.UserQo;
import com.yiran.redis.cache.RedisCacheComponent;

@Service
@Transactional
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RedisCacheComponent<User> cacheComponent;

	public void save(User user) {
		// 删除缓存
		if (CommonUtils.isNotNull(user.getId())) {
			String key = user.getId().toString();
			cacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, key);// 删除原有缓存
		}
		userRepository.save(user);
		// 保存缓存
		if (CommonUtils.isNotNull(user.getId())) {
			String key = user.getId().toString();
			cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, key, user, 12);// 增加缓存，保存12秒
		}
	}

	public void delete(Long id) {
		// 删除缓存
		cacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, id.toString());
		userRepository.deleteById(id);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findOne(Long id) {
		User user = cacheComponent.hashGet(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, id.toString(), User.class);
		if (CommonUtils.isNull(user)) {
			Optional<User> temp = userRepository.findById(id);
			if (temp.isPresent()) {
				user = temp.get();
				cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, id.toString(), user, 12);
			}
		}
		return user;
	}

	public List<User> findByRoleId(Long roleId) {
		return userRepository.findByRoleId(roleId);
	}

	public User findByName(String name) {
		return userRepository.findByName(name);
	}

	public Page<User> findAll(UserQo userQo) {
		Sort sort = new Sort(Sort.Direction.DESC, "created");
		Pageable pageable = PageRequest.of(userQo.getPage(), userQo.getSize(), sort);

		return userRepository.findAll(new Specification<User>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();

				if (CommonUtils.isNotNull(userQo.getName())) {
					predicatesList.add(criteriaBuilder.like(root.get("name"), "%" + userQo.getName() + "%"));
				}
				if (CommonUtils.isNotNull(userQo.getCreated())) {
					predicatesList.add(criteriaBuilder.greaterThan(root.get("created"), userQo.getCreated()));
				}

				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));

				return query.getRestriction();
			}
		}, pageable);
	}

}
