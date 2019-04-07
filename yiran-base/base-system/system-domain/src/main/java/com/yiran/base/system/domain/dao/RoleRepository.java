package com.yiran.base.system.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yiran.base.system.domain.entity.RoleInfo;

@Repository
public interface RoleRepository extends JpaRepository<RoleInfo, Long>, JpaSpecificationExecutor<RoleInfo> {

	@Modifying
	@Query(value = "delete from sys_user_role where role_id = :role_id", nativeQuery = true)
	void cleanUserToRole(@Param("role_id") Long role_id);
}
