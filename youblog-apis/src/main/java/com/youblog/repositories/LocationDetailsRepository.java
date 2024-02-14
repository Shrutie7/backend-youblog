package com.youblog.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.youblog.entities.LocationDetailsEntity;

public interface LocationDetailsRepository extends JpaRepository<LocationDetailsEntity, Long> {

	@Query(value = "select location_id,location_name from location_details where state = :state and city=:city", nativeQuery = true)
	public ArrayList<Object[]> getLocationAddress(String state, String city);

	@Query(value = "select distinct city from location_details where state = :state", nativeQuery = true)
	public ArrayList<Object[]> getCityList(String state);

	@Query(value = "select distinct state from location_details;", nativeQuery = true)
	public ArrayList<Object[]> getStateList();

	
	@Query(value = "SELECT ld.LOCATION_ID,\r\n"
			+ "	ld.LOCATION_NAME\r\n"
			+ "FROM LOCATION_DETAILS ld\r\n"
			+ "inner join gym_details gd on gd.location_id = ld.location_id where state = :state and city=:city", nativeQuery = true)
	public ArrayList<Object[]> getLocationAddressFilter(String state, String city);
	
	
}
