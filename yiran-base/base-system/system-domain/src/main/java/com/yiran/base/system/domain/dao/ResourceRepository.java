package com.yiran.base.system.domain.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yiran.base.system.domain.entity.ResourceInfo;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceInfo, Long>, JpaSpecificationExecutor<ResourceInfo> {

	@Modifying
	@Query(value = "delete from sys_role_resource where resource_id = :resource_id", nativeQuery = true)
	void cleanRoleToResource(@Param("resource_id") Long resource_id);
}
