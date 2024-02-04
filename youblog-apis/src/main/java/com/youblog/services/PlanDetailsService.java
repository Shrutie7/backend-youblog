package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.youblog.payloads.PLanGetRequest;
import com.youblog.payloads.PlanCheckExpiryRequest;
import com.youblog.payloads.PlanCreateRequest;
import com.youblog.payloads.PlanDeleteRequest;
import com.youblog.payloads.PlanEditRequest;
import com.youblog.payloads.PlanListRequest;
import com.youblog.payloads.PlanPurchaseRequest;

public interface PlanDetailsService {

	public ResponseEntity<Map<String,Object>> createPlan(PlanCreateRequest usreq);

	public ResponseEntity<Map<String, Object>> getplanlist(PlanListRequest usreq);
	
	public ResponseEntity<Map<String, Object>> editPlan(PlanEditRequest usreq);
	
	public ResponseEntity<Map<String, Object>> deletePlan(PlanDeleteRequest usreq);
	
	public ResponseEntity<Map<String,Object>> getplan(PLanGetRequest usreq);

	public ResponseEntity<Map<String,Object>> planexpirycheck(PlanCheckExpiryRequest usreq);
	


}
