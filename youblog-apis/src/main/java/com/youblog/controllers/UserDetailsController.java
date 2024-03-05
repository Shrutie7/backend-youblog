package com.youblog.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.payloads.CancelMembershipRequest;
import com.youblog.payloads.ChangeGymLocationRequest;
import com.youblog.payloads.GetUserRequest;
import com.youblog.payloads.PlanPurchaseRequest;
import com.youblog.payloads.TrainerListRequest;
import com.youblog.payloads.UpdatePasswordRequest;
import com.youblog.payloads.UpdateUserRequest;
import com.youblog.payloads.UserDetailsRequest;
import com.youblog.services.UserDetailsService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserDetailsController {
	@Autowired
	private UserDetailsService userDetailsService;

	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserDetailsRequest userDetailsRequest) {
		return userDetailsService.createUser(userDetailsRequest);
	}

	@PostMapping("/getuser")
	public ResponseEntity<Map<String, Object>> getUser(@RequestBody GetUserRequest getUserRequest) {
		return userDetailsService.getUser(getUserRequest);
	}

	@PostMapping("/update")
	public ResponseEntity<Map<String, Object>> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
		return userDetailsService.updateUser(updateUserRequest);
	}

	@PostMapping("/updatePassword")
	public ResponseEntity<Map<String, Object>> updatePassword(
			@RequestBody UpdatePasswordRequest updatePasswordRequest) {
		return userDetailsService.updateUserPassword(updatePasswordRequest);
	}

	@GetMapping("/list")
	public ResponseEntity<Map<String, Object>> getUserList() {
		return userDetailsService.getUserList();
	}

	@PostMapping("/trainerlist")
	public ResponseEntity<Map<String, Object>> trainerList(@RequestBody TrainerListRequest trainerListRequest) {
		return userDetailsService.getTrainerList(trainerListRequest);
	}

	@PostMapping("/plan/purchase")
	public ResponseEntity<Map<String, Object>> planPurchase(@RequestBody PlanPurchaseRequest planPurchaseRequest) {
		return userDetailsService.planPurchase(planPurchaseRequest);
	}
	
	@PostMapping("/cancel/membership")
	public ResponseEntity<Map<String, Object>> cancelMembership(@RequestBody CancelMembershipRequest cancelMembershipRequest) {
		return userDetailsService.cancelMembership(cancelMembershipRequest);
	}
	
	@PostMapping("/change/gym/location")
	public ResponseEntity<Map<String, Object>> changeGymLocation(@RequestBody ChangeGymLocationRequest changeGymLocationRequest) {
		return userDetailsService.changeGymLocation(changeGymLocationRequest);
	}
}
