package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.youblog.payloads.CancelMembershipRequest;
import com.youblog.payloads.ChangeGymLocationRequest;
import com.youblog.payloads.GetUserRequest;
import com.youblog.payloads.PlanPurchaseRequest;
import com.youblog.payloads.TrainerListRequest;
import com.youblog.payloads.UpdatePasswordRequest;
import com.youblog.payloads.UpdateUserRequest;
import com.youblog.payloads.UserDetailsRequest;

public interface UserDetailsService {

	public ResponseEntity<Map<String, Object>> createUser(UserDetailsRequest userDetailsRequest);

	public ResponseEntity<Map<String, Object>> getUser(GetUserRequest getUserRequest);

	public ResponseEntity<Map<String, Object>> updateUser(UpdateUserRequest updateUserRequest);

	public ResponseEntity<Map<String, Object>> updateUserPassword(UpdatePasswordRequest updatePasswordRequest);

	public ResponseEntity<Map<String, Object>> getUserList();

	public ResponseEntity<Map<String, Object>> getTrainerList(TrainerListRequest trainerList);

	public ResponseEntity<Map<String, Object>> planPurchase(PlanPurchaseRequest planPurchaseRequest);

	public ResponseEntity<Map<String, Object>> cancelMembership(CancelMembershipRequest cancelMembershipRequest);

	public ResponseEntity<Map<String, Object>> changeGymLocation(ChangeGymLocationRequest changeGymLocationRequest);

}
