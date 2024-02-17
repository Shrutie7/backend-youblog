package com.youblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youblog.entities.TimeDetailsEntity;

@Repository
public interface TimeDetailsRepository extends JpaRepository<TimeDetailsEntity, Long> { 

	}

