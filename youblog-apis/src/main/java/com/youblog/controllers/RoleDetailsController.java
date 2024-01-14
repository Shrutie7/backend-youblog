package com.youblog.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.services.RoleDetailsService;

@RestController
@RequestMapping("/role")
public class RoleDetailsController {

	@Autowired
	private RoleDetailsService roledetailsservice;
	
	@GetMapping("/list")
	public ResponseEntity<Map<String,Object>> roleList(){
		return roledetailsservice.getRoleList();
	}
}
