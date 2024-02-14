package com.youblog.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.youblog.entities.ImageDetailsEntity;

@Repository
public interface ImageDetailsRepository extends MongoRepository<ImageDetailsEntity, String>{

}
