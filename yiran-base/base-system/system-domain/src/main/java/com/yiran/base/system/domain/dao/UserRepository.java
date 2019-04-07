package com.yiran.base.system.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yiran.base.system.domain.entity.UserInfo;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long>, JpaSpecificationExecutor<UserInfo> {
	@Query("select distinct u from UserInfo u where u.account= :account")
	UserInfo findByAccount(@Param("account") String account);
}
