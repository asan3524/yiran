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
import com.yiran.base.system.domain.dao.KindRepository;
import com.yiran.base.system.domain.entity.Kind;
import com.yiran.base.system.object.KindQo;
import com.yiran.redis.cache.RedisCacheComponent;

@Service
@Transactional
public class KindService {
	@Autowired
	private KindRepository kindRepository;
	@Autowired
	private RedisCacheComponent<Kind> cacheComponent;

	public void save(Kind kind) {
		// 删除缓存
		if (CommonUtils.isNotNull(kind.getId())) {
			String key = kind.getId().toString();
			cacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_KIND_ID, key);// 删除原有缓存
		}
		kindRepository.save(kind);
		// 保存缓存
		if (CommonUtils.isNotNull(kind.getId())) {
			String key = kind.getId().toString();
			cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_KIND_ID, key, kind, 12);// 增加缓存，保存12秒
		}
	}

	public void delete(Long id) {
		// 删除缓存
		cacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_KIND_ID, id.toString());

		kindRepository.deleteById(id);
	}

	public List<Kind> findAll() {
		return kindRepository.findAll();
	}

	public Kind findOne(Long id) {
//		Kind kind = null;
		Kind kind = cacheComponent.hashGet(Constant.YIRAN_BASE_SYSTEM_CENTER_KIND_ID, id.toString(), Kind.class);
		if (CommonUtils.isNull(kind)) {
			Optional<Kind> temp = kindRepository.findById(id);
			if (temp.isPresent()) {
				kind = temp.get();
				cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_KIND_ID, id.toString(), kind, 12);
			}
		}
		return kind;
	}

	public Page<Kind> findAll(KindQo kindQo) {
		Sort sort = new Sort(Sort.Direction.DESC, "created");
		Pageable pageable = PageRequest.of(kindQo.getPage(), kindQo.getSize(), sort);

		return kindRepository.findAll(new Specification<Kind>() {

			private static final long serialVersionUID = -3064673907270291499L;

			@Override
			public Predicate toPredicate(Root<Kind> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();

				if (CommonUtils.isNotNull(kindQo.getName())) {
					predicatesList.add(criteriaBuilder.like(root.get("name"), "%" + kindQo.getName() + "%"));
				}
				if (CommonUtils.isNotNull(kindQo.getCreated())) {
					predicatesList.add(criteriaBuilder.greaterThan(root.get("created"), kindQo.getCreated()));
				}

				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));

				return query.getRestriction();
			}
		}, pageable);
	}

}
