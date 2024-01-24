package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.youblog.payloads.GymCreateRequest;

public interface GymDetailsService {

	public ResponseEntity<Map<String, Object>> creategym(GymCreateRequest usrequest);
}
