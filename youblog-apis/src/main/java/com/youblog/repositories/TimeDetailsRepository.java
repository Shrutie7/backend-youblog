package com.youblog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.TimeDetailsEntity;

@Repository
public interface TimeDetailsRepository extends JpaRepository<TimeDetailsEntity, Long> {

	@Query(value = "select * from time_details where active_flag = true",nativeQuery = true)
	public List<TimeDetailsEntity> getTimeDetails(); 

	}

