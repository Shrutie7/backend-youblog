package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.youblog.payloads.ClassMasterCreateRequest;
import com.youblog.payloads.ClassMasterDeleteRequest;

public interface ClassDetailsService {

	public ResponseEntity<Map<String, Object>> classMasterCreate(ClassMasterCreateRequest classMasterCreateRequest);

	public ResponseEntity<Map<String, Object>> classMasterList();

	public ResponseEntity<Map<String, Object>> classMasterDelete(ClassMasterDeleteRequest classMasterDeleteRequest);

}
