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

import com.youblog.payloads.ClassDetailsCreateRequest;
import com.youblog.payloads.ClassDetailsGetRequest;
import com.youblog.payloads.ClassDetailsListRequest;
import com.youblog.payloads.ClassDetailsListTrainerRequest;
import com.youblog.payloads.ClassDetailsUpdateRequest;
import com.youblog.payloads.ClassMasterCreateRequest;
import com.youblog.payloads.ClassMasterDeleteRequest;
import com.youblog.payloads.ClassUserDataListRquest;
import com.youblog.payloads.ClassUserLeaveRequest;
import com.youblog.payloads.ClassUserMappingRequest;
import com.youblog.services.ClassDetailsService;

@RestController
@RequestMapping("/class")
@CrossOrigin(origins = "*")
public class ClassDetailsController {
	
	@Autowired
	private ClassDetailsService classDetailsService;
	
	@PostMapping("/master/create")
	public ResponseEntity<Map<String,Object>> classMasterCreate(@RequestBody ClassMasterCreateRequest classMasterCreateRequest){
		return classDetailsService.classMasterCreate(classMasterCreateRequest);
	}
	
	@GetMapping("/master/list")
	public ResponseEntity<Map<String,Object>> classMasterList(){
		return classDetailsService.classMasterList();
	}
	
	@PostMapping("/master/delete")
	public ResponseEntity<Map<String,Object>> classMasterDelete(@RequestBody ClassMasterDeleteRequest classMasterDeleteRequest){
		return classDetailsService.classMasterDelete(classMasterDeleteRequest);
	}
	
	@GetMapping("/time/details/list")
	public ResponseEntity<Map<String,Object>> getTimeDetails(){
		return classDetailsService.getTimeDetails();
	}
	
	@PostMapping("/create")
	public ResponseEntity<Map<String,Object>> classDetailsCreate(@RequestBody ClassDetailsCreateRequest classDetailsCreateRequest){
		return classDetailsService.classDetailsCreate(classDetailsCreateRequest);
	}
	
	@PostMapping("/list")
	public ResponseEntity<Map<String,Object>> classDetailsList(@RequestBody ClassDetailsListRequest classDetailsListRequest){
		return classDetailsService.classDetailsList(classDetailsListRequest);
	}
	
	@PostMapping("/list/trainer")
	public ResponseEntity<Map<String,Object>> classDetailsListTrainer(@RequestBody ClassDetailsListTrainerRequest classDetailsListTrainerRequest){
		return classDetailsService.classDetailsListTrainer(classDetailsListTrainerRequest);
	}
	
	@PostMapping("/get")
	public ResponseEntity<Map<String,Object>> classDetailsGet(@RequestBody ClassDetailsGetRequest classDetailsGetRequest){
		return classDetailsService.classDetailsGet(classDetailsGetRequest);
	}
	
	@PostMapping("/update")
	public ResponseEntity<Map<String,Object>> classDetailsUpdate(@RequestBody ClassDetailsUpdateRequest classDetailsUpdateRequest){
		return classDetailsService.classDetailsUpdate(classDetailsUpdateRequest);
	}
	
	@PostMapping("/user/mapping")
	public ResponseEntity<Map<String,Object>> classUserMapping(@RequestBody ClassUserMappingRequest classUserMappingRequest){
		return classDetailsService.classUserMapping(classUserMappingRequest);
	}
	
	@PostMapping("/user/leave")
	public ResponseEntity<Map<String,Object>> classUserLeave(@RequestBody ClassUserLeaveRequest classUserLeaveRequest){
		return classDetailsService.classUserLeave(classUserLeaveRequest);
	}
	
	@PostMapping("/users/list")
	public ResponseEntity<Map<String,Object>> classUsersList(@RequestBody ClassDetailsGetRequest classUsersListRequest){
		return classDetailsService.classUsersList(classUsersListRequest);
	}
	
	@PostMapping("/user/data/list")
	public ResponseEntity<Map<String,Object>> classUserDataList(@RequestBody ClassUserDataListRquest classUserDataListRquest){
		return classDetailsService.classUserDataList(classUserDataListRquest);
	}
}
