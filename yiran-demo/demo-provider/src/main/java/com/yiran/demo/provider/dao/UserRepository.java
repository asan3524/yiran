package com.yiran.demo.provider.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yiran.demo.provider.entiry.UserInfo;


@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {

}
