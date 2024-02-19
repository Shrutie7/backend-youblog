package com.youblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.ClassDetailsEntity;

@Repository
public interface ClassDetailsRepository extends JpaRepository<ClassDetailsEntity, Long> {

	@Query(value = "select case when count(*)>0 then false else true end from class_details cd where cd.time_details_id = :timeDetailsId \r\n"
			+ "and cd.trainer_id = :trainerId and cd.week_day = :weekDay and cd.active_flag = true",nativeQuery = true)
	public Boolean checkTiming(Long trainerId, Long timeDetailsId, String weekDay);

	@Query(value = "select count(*) from class_details cd where \r\n"
			+ "cd.trainer_id = :trainerId and cd.week_day = :weekDay and cd.active_flag = true",nativeQuery = true)
	public int checkCount(Long trainerId, String weekDay); 

	}
