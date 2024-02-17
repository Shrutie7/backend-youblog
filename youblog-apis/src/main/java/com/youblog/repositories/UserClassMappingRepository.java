package com.youblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youblog.entities.UserClassMappingEntity;

@Repository
public interface UserClassMappingRepository extends JpaRepository<UserClassMappingEntity, Long> { 

	}
