package com.youblog.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.youblog.entities.GymDetailsEntity;

public interface GymDetailsRepository extends JpaRepository<GymDetailsEntity, Long> {

	@Query(value = "Select gym_id,gym_name,gym_address,owner_id from gym_details where location_id = :locationId", nativeQuery = true)
	public ArrayList<Object[]> getGymAddressList(Long locationId);
	
	@Query(value="SELECT GD.GYM_ID,\r\n"
			+ "	GD.GYM_NAME,\r\n"
			+ "	GD.GYM_ADDRESS,\r\n"
			+ "	GD.OWNER_ID,\r\n"
			+ "	ud.trainer_count\r\n"
			+ "FROM GYM_DETAILS AS GD\r\n"
			+ "inner JOIN \r\n"
			+ "	(SELECT case when COUNT(*)>0 and count(*)<=(SELECT COUNT(*) AS CATEGORY_COUNT\r\n"
			+ "FROM CATEGORY_DETAILS\r\n"
			+ "WHERE USER_DETAILS_FLAG = TRUE) then true else false end AS TRAINER_COUNT ,\r\n"
			+ "			GYM_ID\r\n"
			+ "		FROM USER_DETAILS \r\n"
			+ "		WHERE ROLE_ID = 3\r\n"
			+ "			AND ACTIVE_FLAG = TRUE\r\n"
			+ "		GROUP BY GYM_ID) as ud on ud.gym_id = gd.gym_id where location_id =:locationId and ud.trainer_count=true;",nativeQuery = true)
	 public List<Object[]> getgymAddressListFilter(Long locationId);
	 
	 @Query(value = "select * from gym_details where gym_id = :gymId",nativeQuery = true)
	 public GymDetailsEntity findByGymId(Long gymId);

	 @Query(value = "select gd.gym_id,gd.gym_name,gd.gym_address,gd.owner_id from worklist_details wd\r\n"
	 		+ "inner join gym_details gd on gd.owner_id = wd.action_user_id and gd.location_id = :locationId\r\n"
	 		+ "where wd.workflow_master_id in (4,6) and wd.worklist_status = 'P'\r\n"
	 		+ "group by gd.gym_id,gd.gym_name,gd.gym_address,gd.owner_id",nativeQuery = true)
	public List<Object[]> getWorklistGyms(Long locationId);

	@Query(value = "select gym_id,gym_name,gym_address,owner_id from gym_details where gym_id not in (\r\n"
			+ "select gd.gym_id from gym_details gd \r\n"
			+ "inner join user_details ud on ud.gym_id = gd.gym_id and ud.category_id=:categoryId and ud.role_id = 3\r\n"
			+ "and (ud.active_flag = true or (ud.active_flag = false and ud.worklist_status = 'P'))\r\n"
			+ "and gd.location_id = :relocatedLocationId) and location_id = :relocatedLocationId group by gym_id,gym_name,gym_address,owner_id",nativeQuery = true)
	public List<Object[]> getGymForTrainerRelocate(Long relocatedLocationId, Long categoryId);

	@Query(value = "select gd.gym_id,gd.gym_name,gd.gym_address,gd.owner_id from worklist_details wd \r\n"
			+ "inner join  gym_details gd on gd.owner_id = wd.action_user_id and gd.location_id = :relocatedLocationId\r\n"
			+ "inner join user_details ud on ud.user_id = wd.initiated_user_id and ud.role_id = 3 and ud.category_id = :categoryId\r\n"
			+ "where wd.workflow_master_id in (4,6) and wd.worklist_status= 'P'\r\n"
			+ "group by gd.gym_id,gd.gym_name,gd.gym_address,gd.owner_id",nativeQuery = true)
	public List<Object[]> getWorklistGymsForTrainerRelocate(Long relocatedLocationId, Long categoryId);

	@Query(value = "select gd.gym_id,gd.gym_name,gd.gym_address,gd.owner_id  from gym_details gd \r\n"
			+ "inner join user_details ud on ud.gym_id = gd.gym_id and ud.category_id = (select category_id from user_details where user_id =\r\n"
			+ "(select parent_user_id from user_details  \r\n"
			+ "where user_id =:userId))\r\n"
			+ "where gd.location_id = :relocatedLocationId group by gd.gym_id,gd.gym_name,gd.gym_address,gd.owner_id",nativeQuery = true)
	public List<Object[]> gymListForUserRelocate(Long relocatedLocationId, Long userId);

}
