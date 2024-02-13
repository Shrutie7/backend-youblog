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

import com.youblog.payloads.GetWorklistDataRequest;
import com.youblog.payloads.GetWorklistDetails;
import com.youblog.payloads.WorklistCreateRequest;
import com.youblog.payloads.WorklistUpdateRequest;
import com.youblog.services.WorklistService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/worklist")
public class WorklistController {
	
	@Autowired
	private WorklistService worklistService;
	
	@PostMapping("/initiate")
	public ResponseEntity<Map<String, Object>> initiateWorkList(
			@RequestBody WorklistCreateRequest worklistCreateRequest) {
		return worklistService.initiateWorkList(worklistCreateRequest);
	}
	
	@PostMapping("/update")
	public ResponseEntity<Map<String, Object>> updateWorkList(
			@RequestBody WorklistUpdateRequest worklistUpdateRequest) {
		return worklistService.updateWorkList(worklistUpdateRequest);
	}
	
	@PostMapping("/getDetails")
	public ResponseEntity<Map<String, Object>> getWorklistData(
			@RequestBody GetWorklistDataRequest getWorklistDataRequest) {
		return worklistService.getWorklistData(getWorklistDataRequest);
	}

	@PostMapping("/pending")
	public ResponseEntity<Map<String, Object>> getPendingWorklist(@RequestBody GetWorklistDetails getWorklistDetails) {
		return worklistService.getPendingWorklist(getWorklistDetails);
	}

	@GetMapping("/completed")
	public ResponseEntity<Map<String, Object>> getCompletedWorklist(@RequestBody GetWorklistDetails getWorklistDetails) {
		return worklistService.getCompletedWorklist(getWorklistDetails);
	}

	@GetMapping("/requested")
	public ResponseEntity<Map<String, Object>> getRequestedWorklist(@RequestBody GetWorklistDetails getWorklistDetails) {
		return worklistService.getRequestedWorklist(getWorklistDetails);
	}
}
