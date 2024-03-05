package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.youblog.payloads.GetWorklistDetails;
import com.youblog.payloads.WorklistCreateRequest;
import com.youblog.payloads.WorklistUpdateRequest;

public interface WorklistService {

	public ResponseEntity<Map<String, Object>> initiateWorkList(WorklistCreateRequest worklistCreateRequest);

	public ResponseEntity<Map<String, Object>> updateWorkList(WorklistUpdateRequest worklistUpdateRequest);

	public ResponseEntity<Map<String, Object>> getPendingWorklist(GetWorklistDetails getPendingWorklist);

	public ResponseEntity<Map<String, Object>> getCompletedWorklist(GetWorklistDetails getCompletedWorklist);

	public ResponseEntity<Map<String, Object>> getRequestedWorklist(GetWorklistDetails getRequestedWorklist);

}
