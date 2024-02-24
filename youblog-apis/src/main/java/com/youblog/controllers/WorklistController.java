package com.youblog.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.payloads.GetWorklistDetails;
import com.youblog.payloads.WorklistUpdateRequest;
import com.youblog.services.WorklistService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/worklist")
public class WorklistController {

	@Autowired
	private WorklistService worklistService;

	@PostMapping("/update")
	public ResponseEntity<Map<String, Object>> updateWorkList(
			@RequestBody WorklistUpdateRequest worklistUpdateRequest) {
		return worklistService.updateWorkList(worklistUpdateRequest);
	}

	@PostMapping("/pending")
	public ResponseEntity<Map<String, Object>> getPendingWorklist(@RequestBody GetWorklistDetails getPendingWorklist) {
		return worklistService.getPendingWorklist(getPendingWorklist);
	}

	@PostMapping("/completed")
	public ResponseEntity<Map<String, Object>> getCompletedWorklist(
			@RequestBody GetWorklistDetails getCompletedWorklist) {
		return worklistService.getCompletedWorklist(getCompletedWorklist);
	}

	@PostMapping("/requested")
	public ResponseEntity<Map<String, Object>> getRequestedWorklist(
			@RequestBody GetWorklistDetails getRequestedWorklist) {
		return worklistService.getRequestedWorklist(getRequestedWorklist);
	}
}
