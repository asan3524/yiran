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
import com.yiran.base.system.domain.dao.ResourceRepository;
import com.yiran.base.system.domain.entity.Resource;
import com.yiran.base.system.object.ResourceQo;
import com.yiran.redis.cache.RedisCacheComponent;

@Service
@Transactional
public class ResourceService {
	@Autowired
	private ResourceRepository resourceRepository;
	@Autowired
	private RedisCacheComponent<Resource> cacheComponent;

	public void save(Resource resource) {
		// 删除缓存
		if (CommonUtils.isNotNull(resource.getId())) {
			String key = resource.getId().toString();
			cacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_RESOURCE_ID, key);// 删除原有缓存
		}
		resourceRepository.save(resource);
		// 保存缓存
		if (CommonUtils.isNotNull(resource.getId())) {
			String key = resource.getId().toString();
			cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_RESOURCE_ID, key, resource, 12);// 增加缓存，保存12秒
		}
	}

	public void delete(Long id) {
		// 删除缓存
		cacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_RESOURCE_ID, id.toString());

		resourceRepository.deleteById(id);
	}

	public List<Resource> findAll() {
		return resourceRepository.findAll();
	}

	public Resource findOne(Long id) {
		Resource resource = cacheComponent.hashGet(Constant.YIRAN_BASE_SYSTEM_CENTER_RESOURCE_ID, id.toString(), Resource.class);
		if (CommonUtils.isNull(resource)) {
			Optional<Resource> temp = resourceRepository.findById(id);
			if (temp.isPresent()) {
				resource = temp.get();
				cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_RESOURCE_ID, id.toString(), resource, 12);
			}
		}
		return resource;
	}

	public Page<Resource> findAll(ResourceQo resourceQo) {
		Sort sort = new Sort(Sort.Direction.DESC, "created");
		Pageable pageable = PageRequest.of(resourceQo.getPage(), resourceQo.getSize(), sort);

		return resourceRepository.findAll(new Specification<Resource>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Resource> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();

				if (CommonUtils.isNotNull(resourceQo.getName())) {
					predicatesList.add(criteriaBuilder.like(root.get("name"), "%" + resourceQo.getName() + "%"));
				}
				if (CommonUtils.isNotNull(resourceQo.getCreated())) {
					predicatesList.add(criteriaBuilder.greaterThan(root.get("created"), resourceQo.getCreated()));
				}

				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));

				return query.getRestriction();
			}
		}, pageable);
	}

}
