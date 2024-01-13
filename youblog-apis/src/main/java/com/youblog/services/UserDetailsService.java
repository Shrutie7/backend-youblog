package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.youblog.payloads.GetUserRequest;
import com.youblog.payloads.UpdatePasswordRequest;
import com.youblog.payloads.UpdateUserRequest;
import com.youblog.payloads.UserDetailsRequest;


public interface UserDetailsService {
	
	
	public ResponseEntity<Map<String, Object>> createUser(UserDetailsRequest usrequest);

	public ResponseEntity<Map<String, Object>> getUser(GetUserRequest usreq);
	
	public ResponseEntity<Map<String, Object>> updateUser(UpdateUserRequest usreq);
	
	
	public ResponseEntity<Map<String, Object>> updateUserPassword(UpdatePasswordRequest usreq);
	
	public ResponseEntity<Map<String,Object>> getUserList();
}
