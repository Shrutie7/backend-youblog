package com.youblog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.youblog.entities.CategoryDetailsEntity;

public interface CategoryDetailsRepository extends JpaRepository<CategoryDetailsEntity, Long> {

	@Query(value = "select * from category_details and post_details_flag = true", nativeQuery = true)
	public List<CategoryDetailsEntity> getCategoryList();

	@Query(value = "select * from category_details cd where cd.category_id not in \n"
			+ "(select cd.category_id from category_details cd \n"
			+ "inner join user_details ud on ud.category_id = cd.category_id and ud.active_flag = true and ud.gym_id = :gymId\n"
			+ "where cd.user_details_flag = true) and\n"
			+ "cd.user_details_flag = true",nativeQuery = true)
	public List<CategoryDetailsEntity> userCategoryList(Long gymId);
}
