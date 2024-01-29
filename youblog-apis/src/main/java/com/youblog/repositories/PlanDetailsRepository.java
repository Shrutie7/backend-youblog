package com.youblog.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.youblog.entities.PlanDetailsEntity;
import com.youblog.entities.UserDetailsEntity;

public interface PlanDetailsRepository extends JpaRepository<PlanDetailsEntity, Long>{

	@Query(value = "SELECT plan_id,plan_name,plan_duration,plan_price,plan_description from plan_details where gym_type_id =:gymTypeId and active_flag = true",nativeQuery=true)
	public ArrayList<Object[]> getplanlist(Long gymTypeId);
	
	@Query(value="select * from plan_details where  plan_id= :planId and active_flag = true",nativeQuery = true)
	public PlanDetailsEntity editplan(Long planId);
	
	
	@Query(value="select * from plan_details as PD where PD.plan_id= :planId and PD.active_flag = true",nativeQuery = true)
	public PlanDetailsEntity deleteplan(Long planId);
	
	@Query(value="SELECT * FROM USER_DETAILS WHERE PLAN_ID =:planId",nativeQuery = true)
	public Long checkuserplanmapping(Long planId);
	
	@Query(value="Select plan_name,plan_duration,plan_price,plan_description,category_id,gym_type_id from plan_details where  plan_id= :planId and active_flag = true",nativeQuery = true)
	public ArrayList<Object[]> getplan(Long planId);
	
    @Query(value="SELECT UD.PLAN_PURCHASED_DATE::date AS extracted_date,\r\n"
    		+ "	PD.PLAN_DURATION,\r\n"
    		+ "	PD.PLAN_ID,\r\n"
    		+ "	UD.USER_ID\r\n"
    		+ "FROM PLAN_DETAILS AS PD\r\n"
    		+ "LEFT JOIN USER_DETAILS AS UD ON UD.PLAN_ID = PD.PLAN_ID\r\n"
    		+ "WHERE UD.USER_ID =:userId",nativeQuery = true)
    public Object[] planexpirycheck(Long userId);
    
	
	
}
