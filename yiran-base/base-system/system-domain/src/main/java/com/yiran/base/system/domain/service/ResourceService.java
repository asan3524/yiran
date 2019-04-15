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
import com.yiran.base.system.domain.dao.ResourceRepository;
import com.yiran.base.system.domain.entity.ResourceInfo;
import com.yiran.base.system.object.Resource;
import com.yiran.base.system.qo.ResourceQo;
import com.yiran.redis.cache.RedisCacheComponent;

@Service
@Transactional
public class ResourceService {
	@Autowired
	private ResourceRepository resourceRepository;
	@Autowired
	private RedisCacheComponent cacheComponent;

	public BaseRespData save(Resource resource) {

		BaseRespData brd = new BaseRespData();

		ResourceInfo resourceInfo = CopyUtil.copy(resource, ResourceInfo.class);

		resourceRepository.save(resourceInfo);

		brd.setCode(Code.SC_OK);
		return brd;
	}

	public BaseRespData delete(String id) {

		BaseRespData brd = new BaseRespData();

		ResourceInfo resourceInfo = resourceRepository.getOne(id);
		if (CommonUtils.isNotNull(resourceInfo)) {
			// 删除缓存
			cacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_RESOURCE_ID, resourceInfo.getId());

			resourceRepository.cleanRoleToResource(id);

			resourceRepository.deleteById(id);

			brd.setCode(Code.SC_OK);
		} else {
			brd.setCode(Code.SC_BAD_REQUEST);
			brd.setMessage("resource by id {" + id + "} is not exist");
		}

		return brd;
	}

	public BaseRespData update(Resource resource) {
		BaseRespData brd = new BaseRespData();

		RespData<Resource> rd = get(resource.getId());

		if (Code.SC_OK == rd.getCode() && null != rd.getData()) {
			resource = Resource.copy(rd.getData(), resource);
		} else {
			brd.setCode(Code.SC_BAD_REQUEST);
			brd.setMessage("update resource that is not exist");
			return brd;
		}

		ResourceInfo resourceInfo = CopyUtil.copy(resource, ResourceInfo.class);

		resourceRepository.save(resourceInfo);

		brd.setCode(Code.SC_OK);
		return brd;
	}

	public BaseRespData load(String id) {
		BaseRespData brd = new BaseRespData();
		Resource resource = null;
		Optional<ResourceInfo> temp = resourceRepository.findById(id);
		if (temp.isPresent()) {
			resource = CopyUtil.copy(temp.get(), Resource.class);
			cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_RESOURCE_ID, resource.getId(), resource);

			brd.setCode(Code.SC_OK);
		} else {
			brd.setCode(Code.SC_BAD_REQUEST);
			brd.setMessage("resource by id {" + id + "} is not exist");
		}
		return brd;
	}

	public RespData<Resource> get(String id) {
		RespData<Resource> rd = new RespData<Resource>();
		Resource resource = cacheComponent.hashGet(Constant.YIRAN_BASE_SYSTEM_CENTER_RESOURCE_ID, id,
				Resource.class);

		if (null != resource) {
			rd.setCode(Code.SC_OK);
			rd.setData(resource);
		} else {
			Optional<ResourceInfo> temp = resourceRepository.findById(id);
			if (temp.isPresent()) {
				resource = CopyUtil.copy(temp.get(), Resource.class);
				cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_RESOURCE_ID, resource.getId(),
						resource);

				rd.setCode(Code.SC_OK);
				rd.setData(resource);
			} else {
				rd.setCode(Code.SC_BAD_REQUEST);
				rd.setMessage("resource by id {" + id + "} is not exist");
			}
		}
		return rd;
	}

	public PageResponseData<List<Resource>> findAll(PageRequestData<ResourceQo> pageRequest) {

		Page<ResourceInfo> result = resourceRepository.findAll(new Specification<ResourceInfo>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ResourceInfo> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();

				if (CommonUtils.isNotNull(pageRequest.getData().getName())) {
					predicatesList
							.add(criteriaBuilder.like(root.get("name"), "%" + pageRequest.getData().getName() + "%"));
				}

				if (CommonUtils.isNotNull(pageRequest.getData().getMethod())) {
					predicatesList.add(criteriaBuilder.equal(root.get("method"), pageRequest.getData().getMethod()));
				}

				if (CommonUtils.isNotNull(pageRequest.getData().getUrl())) {
					predicatesList
							.add(criteriaBuilder.like(root.get("url"), "%" + pageRequest.getData().getUrl() + "%"));
				}
				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));

				return query.getRestriction();
			}
		}, pageRequest.getPageable());

		PageResponseData<List<Resource>> users = new PageResponseData<List<Resource>>(pageRequest);

		users.setTotal(result.getTotalElements());
		users.setNumber(result.getNumberOfElements());
		users.setData(CopyUtil.copyList(result.getContent(), Resource.class));

		users.setCode(Code.SC_OK);
		return users;
	}
}
