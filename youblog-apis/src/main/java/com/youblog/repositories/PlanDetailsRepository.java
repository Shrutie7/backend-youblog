package com.youblog.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.youblog.entities.PlanDetailsEntity;

public interface PlanDetailsRepository extends JpaRepository<PlanDetailsEntity, Long> {

	@Query(value = "SELECT plan_id,plan_name,plan_duration,plan_price,plan_description from plan_details where gym_type_id =:gymTypeId and active_flag = true", nativeQuery = true)
	public ArrayList<Object[]> getPlanList(Long gymTypeId);

	@Query(value = "select * from plan_details where  plan_id= :planId and active_flag = true", nativeQuery = true)
	public PlanDetailsEntity editPlan(Long planId);

	@Query(value = "select * from plan_details as PD where PD.plan_id= :planId and PD.active_flag = true", nativeQuery = true)
	public PlanDetailsEntity deletePlan(Long planId);

	@Query(value = "SELECT * FROM USER_DETAILS WHERE PLAN_ID =:planId", nativeQuery = true)
	public Long checkUserPlanMapping(Long planId);

	@Query(value = "Select plan_name,plan_duration,plan_price,plan_description,category_id,gym_type_id from plan_details where  plan_id= :planId and active_flag = true", nativeQuery = true)
	public ArrayList<Object[]> getPlan(Long planId);

	@Query(value = "SELECT UD.PLAN_PURCHASED_DATE::date AS extracted_date,\r\n" + "	PD.PLAN_DURATION,\r\n"
			+ "	PD.PLAN_ID,\r\n" + "	UD.USER_ID\r\n" + "FROM PLAN_DETAILS AS PD\r\n"
			+ "LEFT JOIN USER_DETAILS AS UD ON UD.PLAN_ID = PD.PLAN_ID\r\n"
			+ "WHERE UD.USER_ID =:userId", nativeQuery = true)
	public Object[] planExpiryCheck(Long userId);

	@Query(value = "SELECT plan_id,plan_name,plan_duration,plan_price,plan_description,features from plan_details where gym_type_id =:gymTypeId and active_flag = true",nativeQuery=true)
	public ArrayList<Object[]> getplanlist(Long gymTypeId);
	
	@Query(value="select * from plan_details where  plan_id= :planId and active_flag = true",nativeQuery = true)
	public PlanDetailsEntity getPlanDetails(Long planId);

	
	@Query(value="Select plan_name,plan_duration,plan_price,plan_description,category_id,gym_type_id,features from plan_details where  plan_id= :planId and active_flag = true",nativeQuery = true)
	public ArrayList<Object[]> getplan(Long planId);
	
    @Query(value="SELECT cast(UD.PLAN_PURCHASED_DATE as date) AS extracted_date,\n"
    		+ "    		PD.PLAN_DURATION,\n"
    		+ "    		PD.PLAN_ID,\n"
    		+ "    		UD.USER_ID\n"
    		+ "    		FROM user_details ud \n"
    		+ "			LEFT join plan_details pd on pd.plan_id = ud.plan_id and pd.active_flag = true\n"
    		+ "			where ud.user_id =:userId and ud.active_flag = true",nativeQuery = true)
    public List<Object[]> planexpirycheck(Long userId);
    
	@Query(value="SELECT case when COUNT(*)>0 then true else false end\n"
			+ "FROM PLAN_DETAILS AS PD \n"
			+ "inner JOIN USER_DETAILS AS UD ON PD.plan_id = UD.plan_id and ud.active_flag = true\n"
			+ "WHERE PD.plan_id = :planId\n"
			+ "	AND pd.active_flag = TRUE",nativeQuery = true)
	public Boolean checkuserplanmapping(Long planId);
}
