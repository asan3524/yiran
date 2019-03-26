package com.yiran.base.system.domain.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yiran.base.system.domain.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	@Query("select distinct u from User u where u.name= :name")
	User findByName(@Param("name") String name);

	@Query("select u from User u " + "left join u.roles r " + "where r.name= :name")
	User findByRoleName(@Param("name") String name);

	@Query("select distinct u from User u where u.id= :id")
	User findByUserId(@Param("id") Long id);

	@Query("select u from User u " + "left join u.roles r " + "where r.id = :id")
	List<User> findByRoleId(@Param("id") Long id);
}
