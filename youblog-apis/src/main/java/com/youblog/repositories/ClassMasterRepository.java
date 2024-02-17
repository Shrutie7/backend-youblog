package com.youblog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.ClassMasterEntity;

@Repository
public interface ClassMasterRepository extends JpaRepository<ClassMasterEntity, Long> {
	
	@Query(value = "select * from class_master where active_flag = true",nativeQuery = true)
	public List<ClassMasterEntity> classMasterList();
}
