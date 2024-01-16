package com.youblog.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.youblog.entities.LocationDetailsEntity;

public interface LocationDetailsRepository extends JpaRepository<LocationDetailsEntity, Long>{
	
	@Query(value="select location_id,location_name from location_details where state = :state and city=:city",nativeQuery = true)
	public ArrayList<Object[]>getlocationaddress (String state,String city);

	@Query(value="select distinct city from location_details where state = :state",nativeQuery = true)
	public ArrayList<Object[]> getcitylist(String state);
	
	@Query(value="select distinct state from location_details;",nativeQuery = true)
	public ArrayList<Object[]> getstatelist();
	
}
