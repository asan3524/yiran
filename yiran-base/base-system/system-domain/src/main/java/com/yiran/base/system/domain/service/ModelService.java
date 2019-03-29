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
import org.springframework.util.StringUtils;

import com.yiran.base.core.util.CommonUtils;
import com.yiran.base.system.domain.constant.Constant;
import com.yiran.base.system.domain.dao.ModelRepository;
import com.yiran.base.system.domain.entity.Model;
import com.yiran.base.system.object.ModelQo;
import com.yiran.redis.cache.RedisCacheComponent;

@Service
@Transactional
public class ModelService {
	@Autowired
	private ModelRepository modelRepository;
	@Autowired
	private RedisCacheComponent<Model> cacheComponent;

	public void save(Model model) {
		// 删除缓存
		if (!StringUtils.isEmpty(model.getId())) {
			String key = model.getId().toString();
			cacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_MODEL_ID, key);// 删除原有缓存
		}
		modelRepository.save(model);
		// 保存缓存
		if (!StringUtils.isEmpty(model.getId())) {
			String key = model.getId().toString();
			cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_MODEL_ID, key, model, 12);// 增加缓存，保存12秒
		}
	}

	public void delete(Long id) {
		// 删除缓存
		cacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_MODEL_ID, id.toString());

		modelRepository.deleteById(id);
	}

	public List<Model> findAll() {
		return modelRepository.findAll();
	}

	public Model findOne(Long id) {
		Model model = cacheComponent.hashGet(Constant.YIRAN_BASE_SYSTEM_CENTER_MODEL_ID, id.toString(), Model.class);
		if (CommonUtils.isNull(model)) {
			Optional<Model> temp = modelRepository.findById(id);
			if (temp.isPresent()) {
				model = temp.get();
				cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_MODEL_ID, id.toString(), model, 12);
			}
		}
		return model;
	}

	public Page<Model> findAll(ModelQo modelQo) {
		Sort sort = new Sort(Sort.Direction.DESC, "created");
		Pageable pageable = PageRequest.of(modelQo.getPage(), modelQo.getSize(), sort);

		return modelRepository.findAll(new Specification<Model>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Model> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();

				if (CommonUtils.isNotNull(modelQo.getName())) {
					predicatesList.add(criteriaBuilder.like(root.get("name"), "%" + modelQo.getName() + "%"));
				}
				if (CommonUtils.isNotNull(modelQo.getCreated())) {
					predicatesList.add(criteriaBuilder.greaterThan(root.get("created"), modelQo.getCreated()));
				}

				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));

				return query.getRestriction();
			}
		}, pageable);
	}

}
