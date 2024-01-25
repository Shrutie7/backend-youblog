package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.youblog.payloads.PlanCreateRequest;

public interface PlanDetailsService {

	public ResponseEntity<Map<String,Object>> createPlan(PlanCreateRequest usreq);
}
