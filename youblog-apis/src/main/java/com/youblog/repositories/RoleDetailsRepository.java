package com.youblog.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.youblog.entities.RoleDetailsEntity;

public interface RoleDetailsRepository extends JpaRepository<RoleDetailsEntity, Long>{
	@Query(value="select * from role_details" ,nativeQuery = true)
	public ArrayList<RoleDetailsEntity> getRoleList();

}
