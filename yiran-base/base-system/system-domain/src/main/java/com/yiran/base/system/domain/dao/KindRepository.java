package com.yiran.base.system.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yiran.base.system.domain.entity.Kind;

@Repository
public interface KindRepository extends JpaRepository<Kind, Long>, JpaSpecificationExecutor<Kind> {

}
