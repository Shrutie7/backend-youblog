package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.youblog.payloads.ClassDetailsCreateRequest;
import com.youblog.payloads.ClassDetailsGetRequest;
import com.youblog.payloads.ClassDetailsListRequest;
import com.youblog.payloads.ClassDetailsListTrainerRequest;
import com.youblog.payloads.ClassDetailsUpdateRequest;
import com.youblog.payloads.ClassMasterCreateRequest;
import com.youblog.payloads.ClassMasterDeleteRequest;
import com.youblog.payloads.ClassUserLeaveRequest;
import com.youblog.payloads.ClassUserMappingRequest;

public interface ClassDetailsService {

	public ResponseEntity<Map<String, Object>> classMasterCreate(ClassMasterCreateRequest classMasterCreateRequest);

	public ResponseEntity<Map<String, Object>> classMasterList();

	public ResponseEntity<Map<String, Object>> classMasterDelete(ClassMasterDeleteRequest classMasterDeleteRequest);

	public ResponseEntity<Map<String, Object>> getTimeDetails();

	public ResponseEntity<Map<String, Object>> classDetailsCreate(ClassDetailsCreateRequest classDetailsCreateRequest);

	public ResponseEntity<Map<String, Object>> classDetailsList(ClassDetailsListRequest classDetailsListRequest);

	public ResponseEntity<Map<String, Object>> classDetailsListTrainer(
			ClassDetailsListTrainerRequest classDetailsListTrainerRequest);

	public ResponseEntity<Map<String, Object>> classDetailsGet(ClassDetailsGetRequest classDetailsGetRequest);

	public ResponseEntity<Map<String, Object>> classDetailsUpdate(ClassDetailsUpdateRequest classDetailsUpdateRequest);

	public ResponseEntity<Map<String, Object>> classUserMapping(ClassUserMappingRequest classUserMappingRequest);

	public ResponseEntity<Map<String, Object>> classUserLeave(ClassUserLeaveRequest classUserLeaveRequest);

	public ResponseEntity<Map<String, Object>> classUsersList(ClassDetailsGetRequest classUsersListRequest);

}
