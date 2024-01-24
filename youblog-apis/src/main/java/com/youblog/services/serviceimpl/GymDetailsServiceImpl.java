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
	GymDetailsRepository gymdetailsrepo;

	@Override
	public ResponseEntity<Map<String, Object>> creategym(GymCreateRequest usrequest) {

			GymDetailsEntity gym = new GymDetailsEntity();
			gym.setGym_name(usrequest.getGymName());
			gym.setContact(usrequest.getContact());
			gym.setLocation_id(usrequest.getLocationId());
			gym.setOwner_id(usrequest.getOwnerId());
			String address = "";
			address = usrequest.getDoorNo() + "," + usrequest.getStreetLane() + "," + usrequest.getPincode();
			gym.setGym_address(address);
			gymdetailsrepo.save(gym);
			return ResponseHandler.response(null, "gym created successfully", true);
		

	}

}
