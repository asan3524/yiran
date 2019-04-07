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
	private RedisCacheComponent<User> userCacheComponent;

	@Autowired
	private RoleService roleService;

	public BaseRespData save(User user) {

		BaseRespData brd = new BaseRespData();
		if (CommonUtils.isNull(user.getAccount())) {
			brd.setCode(Code.SC_BAD_REQUEST);
			brd.setMessage("account can not be null");
			return brd;
		}
		// if (CommonUtils.isNull(user.getEmail())) {
		// brd.setCode(Code.SC_BAD_REQUEST);
		// brd.setMessage("email can not be null");
		// return brd;
		// }
		// if (CommonUtils.isNull(user.getMobile())) {
		// brd.setCode(Code.SC_BAD_REQUEST);
		// brd.setMessage("mobile can not be null");
		// return brd;
		// }

		// if
		// (cacheComponent.hashExist(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID,
		// user.getAccount())) {
		// brd.setCode(Code.SC_BAD_REQUEST);
		// brd.setMessage("account is exist");
		// return brd;
		// }

		List<Role> roles = new ArrayList<Role>();

		user.setId(null);
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
		// 保存成功后缓存
		if (CommonUtils.isNotNull(userInfo.getId())) {

			User userChache = CopyUtil.copy(userInfo, User.class);
			userChache.setRoles(roles);

			userCacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, user.getAccount(), userChache);
		}

		brd.setCode(Code.SC_OK);
		return brd;
	}

	public BaseRespData delete(Long id) {

		BaseRespData brd = new BaseRespData();

		UserInfo userInfo = userRepository.getOne(id);
		if (CommonUtils.isNotNull(userInfo)) {
			// 删除缓存
			userCacheComponent.hashDelete(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, userInfo.getAccount());
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
		if (CommonUtils.isNull(user.getId())) {
			brd.setCode(Code.SC_BAD_REQUEST);
			brd.setMessage("update user id can not be null");
			return brd;
		}

		List<Role> roles = new ArrayList<Role>();
		// 账号不能通过update修改
		user.setAccount(null);
		UserInfo userInfo = CopyUtil.copy(user, UserInfo.class);

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
		// 保存成功后缓存
		if (CommonUtils.isNotNull(userInfo.getId())) {

			User userChache = CopyUtil.copy(userInfo, User.class);
			userChache.setRoles(roles);

			userCacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, user.getAccount(), userChache);
		}

		brd.setCode(Code.SC_OK);
		return brd;
	}

	public RespData<User> get(Long id) {
		RespData<User> rd = new RespData<User>();
		User user = null;
		Optional<UserInfo> temp = userRepository.findById(id);
		if (temp.isPresent()) {
			user = CopyUtil.copy(temp.get(), User.class);
			userCacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, user.getAccount(), user);

			rd.setCode(Code.SC_OK);
			rd.setData(user);
		} else {
			rd.setCode(Code.SC_BAD_REQUEST);
			rd.setMessage("user by id {" + id + "} is not exist");
		}
		return rd;
	}

	public RespData<User> findByAccount(String account) {

		RespData<User> rd = new RespData<User>();

		User user = userCacheComponent.hashGet(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, account, User.class);

		if (null != user) {
			rd.setCode(Code.SC_OK);
			rd.setData(user);
		} else {
			UserInfo userInfo = userRepository.findByAccount(account);

			if (null != userInfo) {
				user = CopyUtil.copy(user, User.class);

				userCacheComponent.hashPut(Constant.YIRAN_BASE_SYSTEM_CENTER_USER_ID, user.getAccount(), user);

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
