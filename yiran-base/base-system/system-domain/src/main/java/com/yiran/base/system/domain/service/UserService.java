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
import com.yiran.base.system.domain.dao.UserRepository;
import com.yiran.base.system.domain.entity.RoleInfo;
import com.yiran.base.system.domain.entity.UserInfo;
import com.yiran.base.system.object.Role;
import com.yiran.base.system.object.User;
import com.yiran.base.system.qo.UserQo;
import com.yiran.redis.cache.RedisCacheComponent;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private RedisCacheComponent<User> userCacheComponent;

	@Autowired
	private RedisCacheComponent<String> cacheComponent;

	public BaseRespData save(User user) {

		BaseRespData brd = new BaseRespData();

		List<Role> roles = new ArrayList<Role>();

		UserInfo userInfo = CopyUtil.copy(user, UserInfo.class);

		if (CommonUtils.isNotNull(user.getRoles())) {

			List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();
			for (Role role : user.getRoles()) {
				if (null != role.getId()) {
					RespData<Role> rtemp = roleService.get(role.getId());

					Role temp = rtemp.getData();

					if (null == temp) {
						brd.setCode(Code.SC_BAD_REQUEST);
						brd.setMessage("save user that role id {" + role.getId() + "} is not exist");
						return brd;
					}
					roles.add(temp);
					roleInfos.add(CopyUtil.copy(temp, RoleInfo.class));
				}
			}
			userInfo.setRoles(roleInfos);
		}

		userRepository.save(userInfo);

		brd.setCode(Code.SC_OK);
		return brd;
	}

	public BaseRespData delete(String id) {

		BaseRespData brd = new BaseRespData();

		UserInfo userInfo = userRepository.getOne(id);
		if (CommonUtils.isNotNull(userInfo)) {
			// 删除缓存
			userCacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, userInfo.getId());
			userRepository.deleteById(id);
			brd.setCode(Code.SC_OK);
		} else {
			brd.setCode(Code.SC_BAD_REQUEST);
			brd.setMessage("user by id {" + id + "} is not exist");
		}

		return brd;
	}

	public BaseRespData update(User user) {
		BaseRespData brd = new BaseRespData();

		RespData<User> rd = get(user.getId());

		if (Code.SC_OK == rd.getCode() && null != rd.getData()) {
			user = User.copy(rd.getData(), user);
		} else {
			brd.setCode(Code.SC_BAD_REQUEST);
			brd.setMessage("update user that is not exist");
			return brd;
		}

		UserInfo userInfo = CopyUtil.copy(user, UserInfo.class);

		List<Role> roles = new ArrayList<Role>();

		if (CommonUtils.isNotNull(user.getRoles())) {

			List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();
			for (Role role : user.getRoles()) {
				if (null != role.getId()) {
					RespData<Role> rtemp = roleService.get(role.getId());

					Role temp = rtemp.getData();
					if (null == temp) {
						brd.setCode(Code.SC_BAD_REQUEST);
						brd.setMessage("update user that role id {" + role.getId() + "} is not exist");
						return brd;
					}
					roles.add(temp);
					roleInfos.add(CopyUtil.copy(temp, RoleInfo.class));
				}
			}
			userInfo.setRoles(roleInfos);
		}

		userRepository.save(userInfo);

		brd.setCode(Code.SC_OK);
		return brd;
	}

	public BaseRespData load(String id) {
		BaseRespData brd = new BaseRespData();
		User user = null;
		Optional<UserInfo> temp = userRepository.findById(id);
		if (temp.isPresent()) {
			user = CopyUtil.copy(temp.get(), User.class);
			userCacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, id, user);

			brd.setCode(Code.SC_OK);
		} else {
			brd.setCode(Code.SC_BAD_REQUEST);
			brd.setMessage("user by id {" + id + "} is not exist");
		}
		return brd;
	}

	public RespData<User> get(String id) {
		RespData<User> rd = new RespData<User>();
		User user = userCacheComponent.hashGet(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, id, User.class);

		if (null != user) {
			rd.setCode(Code.SC_OK);
			rd.setData(user);
		} else {
			Optional<UserInfo> temp = userRepository.findById(id);
			if (temp.isPresent()) {
				user = CopyUtil.copy(temp.get(), User.class);
				userCacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, id, user);

				rd.setCode(Code.SC_OK);
				rd.setData(user);
			} else {
				rd.setCode(Code.SC_BAD_REQUEST);
				rd.setMessage("user by id {" + id + "} is not exist");
			}
		}
		return rd;
	}

	public RespData<User> findByAccount(String account) {

		RespData<User> rd = new RespData<User>();

		String id = cacheComponent.hashGet(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ACCOUNT, account, String.class);

		User user = null;

		if (null != id) {
			user = userCacheComponent.hashGet(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, id, User.class);

			if (null != user) {
				if (user.getAccount().equals(account)) {
					rd.setCode(Code.SC_OK);
					rd.setData(user);
				} else {
					user = null;
					cacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ACCOUNT, account);
					userCacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, id);
				}
			}
		}

		if (null != user) {
			rd.setCode(Code.SC_OK);
			rd.setData(user);
		} else {
			UserInfo userInfo = userRepository.findByAccount(account);

			if (null != userInfo) {
				user = CopyUtil.copy(user, User.class);

				userCacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, user.getId(), user);

				cacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ACCOUNT, user.getAccount(), user.getId());

				rd.setCode(Code.SC_OK);
				rd.setData(user);
			} else {
				rd.setCode(Code.SC_BAD_REQUEST);
				rd.setMessage("user by account {" + account + "} is not exist");
			}
		}

		return rd;
	}

	public PageResponseData<List<User>> findPage(PageRequestData<UserQo> pageRequest) {

		Page<UserInfo> result = userRepository.findAll(new Specification<UserInfo>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<UserInfo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicatesList = new ArrayList<Predicate>();

				if (CommonUtils.isNotNull(pageRequest.getData().getAccount())) {
					predicatesList.add(
							criteriaBuilder.like(root.get("account"), "%" + pageRequest.getData().getAccount() + "%"));
				}
				if (CommonUtils.isNotNull(pageRequest.getData().getName())) {
					predicatesList
							.add(criteriaBuilder.like(root.get("name"), "%" + pageRequest.getData().getName() + "%"));
				}
				if (CommonUtils.isNotNull(pageRequest.getData().getEmail())) {
					predicatesList
							.add(criteriaBuilder.like(root.get("email"), "%" + pageRequest.getData().getEmail() + "%"));
				}
				if (CommonUtils.isNotNull(pageRequest.getData().getMobile())) {
					predicatesList.add(
							criteriaBuilder.like(root.get("mobile"), "%" + pageRequest.getData().getMobile() + "%"));
				}

				query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));

				return query.getRestriction();
			}
		}, pageRequest.getPageable());

		PageResponseData<List<User>> users = new PageResponseData<List<User>>(pageRequest);

		users.setTotal(result.getTotalElements());
		users.setNumber(result.getNumberOfElements());
		users.setData(CopyUtil.copyList(result.getContent(), User.class));

		users.setCode(Code.SC_OK);
		return users;
	}

}
