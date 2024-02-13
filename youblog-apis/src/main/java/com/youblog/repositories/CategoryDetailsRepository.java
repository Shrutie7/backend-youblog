package com.youblog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.youblog.entities.CategoryDetailsEntity;

public interface CategoryDetailsRepository extends JpaRepository<CategoryDetailsEntity, Long> {

	@Query(value = "select * from category_details and post_details_flag = true", nativeQuery = true)
	public List<CategoryDetailsEntity> getCategoryList();

	public List<CategoryDetailsEntity> userCategoryList(Long gymId);
}
