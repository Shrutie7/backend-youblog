package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.youblog.payloads.CalculateRefundRequest;
import com.youblog.payloads.PlanCheckExpiryRequest;
import com.youblog.payloads.PlanCreateRequest;
import com.youblog.payloads.PlanDeleteRequest;
import com.youblog.payloads.PlanEditRequest;
import com.youblog.payloads.PlanGetRequest;
import com.youblog.payloads.PlanListRequest;

public interface PlanDetailsService {

	public ResponseEntity<Map<String, Object>> createPlan(PlanCreateRequest planCreateRequest);

	public ResponseEntity<Map<String, Object>> planList(PlanListRequest planListRequest);

	public ResponseEntity<Map<String, Object>> editPlan(PlanEditRequest planEditRequest);

	public ResponseEntity<Map<String, Object>> deletePlan(PlanDeleteRequest planDeleteRequest);

	public ResponseEntity<Map<String, Object>> getPlan(PlanGetRequest planGetRequest);

	public ResponseEntity<Map<String, Object>> checkPlanExpiry(PlanCheckExpiryRequest planCheckExpiryRequest);

	public ResponseEntity<Map<String, Object>> calculateRefund(CalculateRefundRequest calculateRefundRequest);

}
