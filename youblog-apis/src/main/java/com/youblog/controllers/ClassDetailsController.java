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
import com.youblog.payloads.ClassDetailsListRequest;
import com.youblog.payloads.ClassDetailsListTrainerRequest;
import com.youblog.payloads.ClassMasterCreateRequest;
import com.youblog.payloads.ClassMasterDeleteRequest;
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
	
	
}
