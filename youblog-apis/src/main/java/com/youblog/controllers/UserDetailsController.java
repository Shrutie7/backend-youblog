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

import com.youblog.payloads.GetUserRequest;
import com.youblog.payloads.UpdatePasswordRequest;
import com.youblog.payloads.UpdateUserRequest;
import com.youblog.payloads.UserDetailsRequest;
import com.youblog.services.UserDetailsService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserDetailsController {
	@Autowired
	private UserDetailsService userdetailsservice;
	
	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> createUser (@RequestBody UserDetailsRequest usreq ) {
		return userdetailsservice.createUser(usreq);

	}
	@PostMapping("/getuser")
	public ResponseEntity<Map<String, Object>> getUser(@RequestBody GetUserRequest usreq ) {
		return userdetailsservice.getUser(usreq);

	}
	@PostMapping("/update")
	public ResponseEntity<Map <String,Object>> updateUser(@RequestBody UpdateUserRequest usreq){
		return userdetailsservice.updateUser(usreq);
	}
	
	@PostMapping("/updatePassword")
	public ResponseEntity<Map <String,Object>> updatePassword(@RequestBody UpdatePasswordRequest usreq){
		return userdetailsservice.updateUserPassword(usreq);
	}
	
	@GetMapping("/list")
	public ResponseEntity<Map<String,Object>> userList(){
		return userdetailsservice.getUserList();
	}
		
	
	
	
	
}
