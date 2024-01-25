package com.youblog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.PostDetailsEntity;
@Repository
public interface PostDetailsRepository extends JpaRepository<PostDetailsEntity, Long>{

	@Query(value = "select * from post_details pd where pd.active_flag = true and pd.archive_flag=false and pd.category_id = :categoryId and pd.user_id in \r\n"
			+ "(with recursive userids as (select user_id,parent_user_id,gym_id,role_id from user_details \r\n"
			+ "						   where gym_id = (select ud.gym_id from user_details ud where ud.user_id = :userId) and active_flag=true  union all \r\n"
			+ "						   select ud.user_id,ud.parent_user_id,ud.gym_id,ud.role_id from user_details as ud inner join\r\n"
			+ "										userids as ui on ud.user_id = ui.parent_user_id)\r\n"
			+ "			select distinct(user_id) from userids)",nativeQuery = true)
	public List<PostDetailsEntity> postListWithCategory(Long userId, Long categoryId);

	@Query(value = "select * from post_details pd where pd.active_flag = true and pd.archive_flag=false and pd.user_id in \r\n"
			+ "(with recursive userids as (select user_id,parent_user_id,gym_id,role_id from user_details \r\n"
			+ "						   where gym_id = (select ud.gym_id from user_details ud where ud.user_id = :userId) and active_flag=true union all \r\n"
			+ "						   select ud.user_id,ud.parent_user_id,ud.gym_id,ud.role_id from user_details as ud inner join\r\n"
			+ "										userids as ui on ud.user_id = ui.parent_user_id)\r\n"
			+ "			select distinct(user_id) from userids)",nativeQuery = true)
	public List<PostDetailsEntity> postListWithUserId(Long userId);

	@Query(value = " select * from post_details pd where pd.active_flag = true and pd.archive_flag=false and pd.category_id = :categoryId and pd.user_id in \r\n"
			+ "(select user_id from user_details ud where ud.gym_id = \r\n"
			+ "(select gym_id from gym_details gd where owner_id = :userId limit 1)\r\n"
			+ "and ud.active_flag=true and role_id = 3) or pd.user_id in (1,:userId)",nativeQuery = true)
	public List<PostDetailsEntity> postListWithCategoryForOwner(Long userId, Long categoryId);

	@Query(value = " select * from post_details pd where pd.active_flag = true and pd.archive_flag=false and pd.user_id in \r\n"
			+ "(select user_id from user_details ud where ud.gym_id = \r\n"
			+ "(select gym_id from gym_details gd where owner_id = :userId limit 1)\r\n"
			+ "and ud.active_flag=true and role_id = 3) or pd.user_id in (1,:userId)",nativeQuery = true)
	public List<PostDetailsEntity> postListWithUserIdForOwner(Long userId);
	
}
