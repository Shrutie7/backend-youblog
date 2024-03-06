package com.youblog.services.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.GymDetailsEntity;
import com.youblog.payloads.GymCreateRequest;
import com.youblog.payloads.GymListForTrainerRelocateRequest;
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

	@Override
	public ResponseEntity<Map<String, Object>> gymListForTrainerRelocate(
			GymListForTrainerRelocateRequest gymListForTrainerRelocateRequest) {
		if (gymListForTrainerRelocateRequest.getRelocatedLocationId() != null) {
			if (gymListForTrainerRelocateRequest.getCategoryId() != null) {
				Map<String, Object> data = new HashMap<>();
				List<Object[]> getgymaddress = gymDetailsRepository.getGymForTrainerRelocate(
						gymListForTrainerRelocateRequest.getRelocatedLocationId(),
						gymListForTrainerRelocateRequest.getCategoryId());
				List<Object[]> worklistGyms = gymDetailsRepository.getWorklistGymsForTrainerRelocate(
						gymListForTrainerRelocateRequest.getRelocatedLocationId(),
						gymListForTrainerRelocateRequest.getCategoryId());
				Set<Map<String, Object>> gymaddresslist = new HashSet<>();
				if (getgymaddress == null) {
					data.put("gymAddressList", new ArrayList<>());
					return ResponseHandler.response(data, "gym address list not found", false);

				} else {

					getgymaddress.forEach(ele -> {
						Map<String, Object> gymaddresslist1 = new HashMap<>();
						gymaddresslist1.put("gymId", ele[0].toString());
						gymaddresslist1.put("gymName", ele[1].toString());
						gymaddresslist1.put("gymAddress", ele[2].toString());
						gymaddresslist1.put("ownerId", ele[3].toString());
						gymaddresslist.add(gymaddresslist1);
					});
					worklistGyms.forEach(ele -> {
						Map<String, Object> gymaddresslist1 = new HashMap<>();
						gymaddresslist1.put("gymId", ele[0].toString());
						gymaddresslist1.put("gymName", ele[1].toString());
						gymaddresslist1.put("gymAddress", ele[2].toString());
						gymaddresslist1.put("ownerId", ele[3].toString());
						gymaddresslist.add(gymaddresslist1);
					});
					data.put("gymAddressList", gymaddresslist);
					return ResponseHandler.response(data, "gym address list found", true);
				}
			} else {
				return ResponseHandler.response(null, "Please provide Category Id", false);
			}
		} else {
			return ResponseHandler.response(null, "Please provide location Id", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> gymListForUserRelocate(
			GymListForTrainerRelocateRequest gymListForTrainerRelocateRequest) {
		if (gymListForTrainerRelocateRequest.getRelocatedLocationId() != null) {
			if (gymListForTrainerRelocateRequest.getUserId() != null) {
				Map<String, Object> data = new HashMap<>();
				List<Object[]> getgymaddress = gymDetailsRepository.gymListForUserRelocate(
						gymListForTrainerRelocateRequest.getRelocatedLocationId(),
						gymListForTrainerRelocateRequest.getUserId());
				Set<Map<String, Object>> gymaddresslist = new HashSet<>();
				if (getgymaddress == null) {
					data.put("gymAddressList", new ArrayList<>());
					return ResponseHandler.response(data, "gym address list not found", false);

				} else {

					getgymaddress.forEach(ele -> {
						Map<String, Object> gymaddresslist1 = new HashMap<>();
						gymaddresslist1.put("gymId", ele[0].toString());
						gymaddresslist1.put("gymName", ele[1].toString());
						gymaddresslist1.put("gymAddress", ele[2].toString());
						gymaddresslist1.put("ownerId", ele[3].toString());
						gymaddresslist.add(gymaddresslist1);
					});
					data.put("gymAddressList", gymaddresslist);
					return ResponseHandler.response(data, "gym address list found", true);
				}
			} else {
				return ResponseHandler.response(null, "Please provide user Id", false);
			}
		} else {
			return ResponseHandler.response(null, "Please provide location Id", false);
		}
	}

}
