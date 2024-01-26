package com.youblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.youblog.entities.PostSaveDetailsEntity;

@Repository
public interface PostSaveDetailsRepository extends JpaRepository<PostSaveDetailsEntity, Long>{

}
