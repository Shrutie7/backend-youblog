package com.youblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youblog.entities.WorkflowMasterEntity;

@Repository
public interface WorkflowMasterRepository extends JpaRepository<WorkflowMasterEntity, Long>{

}
