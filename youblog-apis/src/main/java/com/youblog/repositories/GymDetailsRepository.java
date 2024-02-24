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

}
