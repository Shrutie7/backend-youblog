package com.youblog.services.serviceimpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.GymDetailsEntity;
import com.youblog.payloads.GymCreateRequest;
import com.youblog.repositories.GymDetailsRepository;
import com.youblog.services.GymDetailsService;
import com.youblog.utils.ResponseHandler;

@Service
public class GymDetailsServiceImpl implements GymDetailsService {

	@Autowired
	GymDetailsRepository gymDetailsRepository;

	@Override
	public ResponseEntity<Map<String, Object>> createGym(GymCreateRequest gymCreateRequest) {

		GymDetailsEntity gym = new GymDetailsEntity();
		gym.setGymName(gymCreateRequest.getGymName());
		gym.setContact(gymCreateRequest.getContact());
		gym.setLocationId(gymCreateRequest.getLocationId());
		gym.setOwnerId(gymCreateRequest.getOwnerId());
		String address = "";
		address = gymCreateRequest.getDoorNo() + "," + gymCreateRequest.getStreetLane() + ","
				+ gymCreateRequest.getPincode();
		gym.setGymAddress(address);
		gymDetailsRepository.save(gym);
		return ResponseHandler.response(null, "gym created successfully", true);

	}

}
