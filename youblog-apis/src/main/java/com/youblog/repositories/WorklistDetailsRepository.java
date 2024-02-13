package com.youblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youblog.entities.WorklistDetailsEntity;

@Repository
public interface WorklistDetailsRepository extends JpaRepository<WorklistDetailsEntity, Long>{

}
