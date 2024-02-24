package com.youblog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.WorklistDetailsEntity;

@Repository
public interface WorklistDetailsRepository extends JpaRepository<WorklistDetailsEntity, Long>{

	@Query(value = "select wd.*,concat(ud.first_name,' ',ud.last_name) as username,ud.email_id,\r\n"
			+ "ud.role_id,ud.category_id,ud.image_id,ud.active_flag from worklist_details wd \r\n"
			+ "inner join user_details ud on ud.user_id = wd.initiated_user_id \r\n"
			+ "where wd.worklist_status = :status and wd.action_user_id = :userId",nativeQuery = true)
	public List<Object[]> getWorklistData(String status, Long userId);

	@Query(value = "select wd.*,concat(ud.first_name,' ',ud.last_name) as username,ud.email_id,\r\n"
			+ "ud.role_id,ud.category_id,ud.image_id,ud.active_flag from worklist_details wd \r\n"
			+ "inner join user_details ud on ud.user_id = wd.action_user_id \r\n"
			+ "where wd.worklist_status = :status and wd.initiated_user_id = :userId",nativeQuery = true)
	public List<Object[]> getRequestedWorklistData(String status, Long userId);

}
