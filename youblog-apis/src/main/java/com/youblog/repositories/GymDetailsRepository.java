package com.youblog.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.youblog.entities.GymDetailsEntity;

public interface GymDetailsRepository extends JpaRepository<GymDetailsEntity, Long> {

	@Query(value = "Select gym_id,gym_name,gym_address,owner_id from gym_details where location_id = :location_id", nativeQuery = true)
	public ArrayList<Object[]> getGymAddressList(Long location_id);

}
