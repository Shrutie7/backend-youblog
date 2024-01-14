package com.youblog.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.UserDetailsEntity;
import com.youblog.payloads.GetUserRequest;

@Repository

//int[] arr = new int [5]; -- array allocate memory while initializing only cant change at run time
//ArrayList<Integer> arraylist = new ArrayList<>(); --dynamic allocate memory at run time 


//Long here is datatype of pkey
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Long> {
	
	@Query(value = "select * from user_details where email_id = :email",nativeQuery = true)
	public  UserDetailsEntity checkemail(String email);
	
	@Query(value="select * from user_details where email_id = :email and active_flag = true",nativeQuery = true)
	public UserDetailsEntity getUserDetails(String email);
	
	
	@Query(value="select * from user_details where user_id = :userId and active_flag = true",nativeQuery = true)
	public UserDetailsEntity updateUserDetails(Long userId);
	
	@Query(value="select email_id,first_name,gender from user_details where active_flag = true",nativeQuery = true)
	public ArrayList<Object[]> getUserList();

//	select * from youblog_users.user_details where email_id = 'shruti@gmail.com'
}
