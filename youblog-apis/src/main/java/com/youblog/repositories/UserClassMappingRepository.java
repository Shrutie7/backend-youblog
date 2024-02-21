package com.youblog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.UserClassMappingEntity;

@Repository
public interface UserClassMappingRepository extends JpaRepository<UserClassMappingEntity, Long> {

	@Query(value = "select case when count(*)>0 then false else true end from class_details cd \r\n"
			+ "inner join (select cd.week_day,cd.time_details_id from user_class_mapping ucm\r\n"
			+ "inner join class_details cd on cd.class_details_id = ucm.class_details_id\r\n"
			+ "and cd.active_flag = true where ucm.active_flag=true and ucm.user_id = :classDetailsId)clasd on \r\n"
			+ "clasd.week_day = cd.week_day and clasd.time_details_id = cd.time_details_id \r\n"
			+ "where cd.active_flag = true and cd.class_details_id=:userId", nativeQuery = true)
	public Boolean checkForTime(Long userId, Long classDetailsId);

	@Query(value = "select * from user_class_mapping where user_class_mapping_id = :userClassMappingId and active_flag = true", nativeQuery = true)
	public UserClassMappingEntity getActiveMapping(Long userClassMappingId);

	@Query(value = "select ucm.user_class_mapping_id,ud.user_id,ucm.class_details_id,concat(ud.first_name,' ',ud.last_name) as user_name,\r\n"
			+ "ud.email_id,ud.gender,ud.role_id,ud.gym_id,ud.parent_user_id,ud.image_id from user_class_mapping ucm\r\n"
			+ "inner join user_details ud on ud.user_id = ucm.user_id and ud.active_flag = true\r\n"
			+ "where ucm.class_details_id = :classDetailsId and ucm.active_flag = true",nativeQuery = true)
	public List<Object[]> classUsersList(Long classDetailsId);

}
