package com.youblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youblog.entities.ClassDetailsEntity;

@Repository
public interface ClassDetailsRepository extends JpaRepository<ClassDetailsEntity, Long> { 

	}
