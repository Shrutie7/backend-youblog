package com.youblog.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.youblog.entities.CategoryDetailsEntity;

public interface CategoryDetailsRepository extends JpaRepository<CategoryDetailsEntity, Long>{

	@Query(value="select * from category_details",nativeQuery = true)
	public ArrayList<CategoryDetailsEntity> getCategoryList();
}
