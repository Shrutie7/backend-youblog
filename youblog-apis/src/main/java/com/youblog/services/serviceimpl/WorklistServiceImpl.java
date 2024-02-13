package com.youblog.services.serviceimpl;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.payloads.GetWorklistDataRequest;
import com.youblog.payloads.GetWorklistDetails;
import com.youblog.payloads.WorklistCreateRequest;
import com.youblog.payloads.WorklistUpdateRequest;
import com.youblog.services.WorklistService;

@Service
public class WorklistServiceImpl implements WorklistService{

	@Override
	public ResponseEntity<Map<String, Object>> initiateWorkList(WorklistCreateRequest worklistCreateRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Map<String, Object>> updateWorkList(WorklistUpdateRequest worklistUpdateRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Map<String, Object>> getWorklistData(GetWorklistDataRequest getWorklistDataRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Map<String, Object>> getPendingWorklist(GetWorklistDetails getWorklistDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Map<String, Object>> getCompletedWorklist(GetWorklistDetails getWorklistDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Map<String, Object>> getRequestedWorklist(GetWorklistDetails getWorklistDetails) {
		// TODO Auto-generated method stub
		return null;
	}

}
