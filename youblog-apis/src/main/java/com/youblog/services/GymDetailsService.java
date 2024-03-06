package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.youblog.payloads.GymCreateRequest;
import com.youblog.payloads.GymListForTrainerRelocateRequest;

public interface GymDetailsService {

	public ResponseEntity<Map<String, Object>> createGym(GymCreateRequest gymCreateRequest);

	public ResponseEntity<Map<String, Object>> gymListForTrainerRelocate(
			GymListForTrainerRelocateRequest gymListForTrainerRelocateRequest);

	public ResponseEntity<Map<String, Object>> gymListForUserRelocate(
			GymListForTrainerRelocateRequest gymListForTrainerRelocateRequest);
}
