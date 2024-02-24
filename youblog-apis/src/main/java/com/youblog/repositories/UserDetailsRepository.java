package com.youblog.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.UserDetailsEntity;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Long> {

	@Query(value = "select * from user_details where email_id = :email", nativeQuery = true)
	public UserDetailsEntity checkEmail(String email);

	@Query(value = "\r\n" + "			SELECT UD.USER_NAME,\r\n" + "				UD.EMAIL_ID,\r\n"
			+ "				UD.FIRST_NAME,\r\n" + "				UD.LAST_NAME,\r\n" + "				UD.ROLE_ID,\r\n"
			+ "				UD.GENDER,\r\n" + "				RD.ROLE_NAME,\r\n" + "			 UD.USER_ID,	\r\n"
			+ "			 UD.GYM_ID,\r\n" + "				UD.PARENT_USER_ID,\r\n"
			+ "				USD.USER_NAME AS PARENT_USER_NAME,\r\n"
			+ "				USD.FIRST_NAME AS PARENT_FIRST_NAME,\r\n"
			+ "				USD.LAST_NAME AS PARENT_LAST_NAME,\r\n" + "				USD.ROLE_ID AS PARENT_ROLE_ID,\r\n"
			+ "				LD.LOCATION_ID,\r\n" + "				LD.STATE,\r\n" + "				LD.CITY,\r\n"
			+ "				LD.LOCATION_NAME,\r\n" + "				PD.PLAN_ID,\r\n"
			+ "				PD.PLAN_NAME,\r\n" + "				TO_CHAR(UD.PLAN_PURCHASED_DATE,\r\n"
			+ "			\r\n" + "					'DD Mon YY') AS PLAN_START_DATE,\r\n" + "					\r\n"
			+ "				case when GD.gym_name ='YouFit Gyms Elite' then 2 else case when gd.gym_name = 'YouFit Gyms Pro' then 1 end end as gym_type_id, \r\n"
			+ "			PD.PLAN_DURATION, to_char(UD.PLAN_PURCHASED_DATE + INTERVAL '1 MONTH' * pd.plan_duration, 'DD Mon YY') as plan_end_date,ud.image_id\r\n"
			+ "			FROM USER_DETAILS AS UD\r\n"
			+ "			LEFT JOIN ROLE_DETAILS AS RD ON UD.ROLE_ID = RD.ROLE_ID\r\n"
			+ "			LEFT JOIN USER_DETAILS AS USD ON USD.USER_ID = UD.PARENT_USER_ID\r\n"
			+ "			LEFT JOIN LOCATION_DETAILS AS LD ON LD.LOCATION_ID = UD.LOCATION_ID\r\n"
			+ "			LEFT JOIN PLAN_DETAILS AS PD ON PD.PLAN_ID = UD.PLAN_ID\r\n"
			+ "			LEFT JOIN gym_details AS GD ON GD.gym_id = UD.gym_id\r\n"
			+ "			WHERE UD.EMAIL_ID = :email", nativeQuery = true)
	public ArrayList<Object[]> getUserDetails(String email);

	@Query(value = "select * from user_details where user_id = :userId and active_flag = true", nativeQuery = true)
	public UserDetailsEntity updateUserDetails(Long userId);

	@Query(value = "select * from user_details where active_flag = true", nativeQuery = true)
	public ArrayList<UserDetailsEntity> getUserList();

	@Query(value = "SELECT ROUND(cast(AVG(FD.RATING) as numeric),2) AS rating,\r\n"
			+ "	CONCAT(UD.FIRST_NAME,\r\n" + "\r\n" + "		' ',\r\n" + "		UD.LAST_NAME) AS trainerName,\r\n"
			+ "	UD.USER_ID,\r\n" + "	UD.CATEGORY_ID,\r\n" + "	CD.CATEGORY_NAME,ud.image_id\r\n"
			+ "FROM USER_DETAILS AS UD\r\n" + "LEFT JOIN FEEDBACK_DETAILS AS FD ON UD.USER_ID = FD.TRAINER_USER_ID\r\n"
			+ "LEFT JOIN CATEGORY_DETAILS AS CD ON CD.CATEGORY_ID = UD.CATEGORY_ID\r\n" + "WHERE ROLE_ID = 3\r\n"
			+ "	AND ud.gym_id =:gymId\r\n" + "GROUP BY CONCAT(UD.FIRST_NAME,\r\n" + "\r\n"
			+ "	' ',\r\n"
			+ "	UD.LAST_NAME),\r\n" + "	UD.USER_ID,\r\n" + "	UD.CATEGORY_ID,\r\n"
			+ "	CD.CATEGORY_NAME", nativeQuery = true)
	public ArrayList<Object[]> getTrainerList(Long gymId);

	@Query(value = "select * from user_details where user_id = :userId", nativeQuery = true)
	public UserDetailsEntity findByUserId(Long userId);
}
