package com.youblog.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.payloads.CategoryListRequest;
import com.youblog.payloads.FeedbackCreateRequest;
import com.youblog.payloads.FeedbackListRequest;
import com.youblog.payloads.GymCreateRequest;
import com.youblog.payloads.PlanCheckExpiryRequest;
import com.youblog.payloads.PlanCreateRequest;
import com.youblog.payloads.PlanDeleteRequest;
import com.youblog.payloads.PlanEditRequest;
import com.youblog.payloads.PlanGetRequest;
import com.youblog.payloads.PlanListRequest;
import com.youblog.services.CategoryDetailsService;
import com.youblog.services.FeedbackService;
import com.youblog.services.GymDetailsService;
import com.youblog.services.PlanDetailsService;
import com.youblog.services.RoleDetailsService;

@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
public class CommonController {
	@Autowired
	private CategoryDetailsService categoryDetailsService;

	@Autowired
	private GymDetailsService gymDetailsService;

	@Autowired
	private RoleDetailsService roleDetailsService;

	@Autowired
	private FeedbackService feedBackService;

	@Autowired
	private PlanDetailsService planDetailsService;

	@GetMapping("/categorylist")
	public ResponseEntity<Map<String, Object>> categoryList() {
		return categoryDetailsService.categoryList();
	}

	@GetMapping("/rolelist")
	public ResponseEntity<Map<String, Object>> roleList() {
		return roleDetailsService.roleList();
	}

	@PostMapping("/gymcreate")
	public ResponseEntity<Map<String, Object>> createGym(@RequestBody GymCreateRequest gymCreateRequest) {
		return gymDetailsService.createGym(gymCreateRequest);
	}

	@PostMapping("/feedbackcreate")
	public ResponseEntity<Map<String, Object>> createFeedback(
			@RequestBody FeedbackCreateRequest feedbackCreateRequest) {
		return feedBackService.createFeedback(feedbackCreateRequest);
	}

	@PostMapping("/feedback/list")
	public ResponseEntity<Map<String, Object>> feedbackList(@RequestBody FeedbackListRequest feedbackListRequest) {
		return feedBackService.feedbackList(feedbackListRequest);
	}

	@PostMapping("/plan/create")
	public ResponseEntity<Map<String, Object>> createPlan(@RequestBody PlanCreateRequest planCreateRequest) {
		return planDetailsService.createPlan(planCreateRequest);
	}

	@PostMapping("/plan/list")
	public ResponseEntity<Map<String, Object>> planList(@RequestBody PlanListRequest planListRequest) {
		return planDetailsService.planList(planListRequest);
	}

	@PostMapping("/plan/edit")
	public ResponseEntity<Map<String, Object>> editplan(@RequestBody PlanEditRequest planEditRequest) {
		return planDetailsService.editPlan(planEditRequest);
	}

	@PostMapping("/plan/delete")
	public ResponseEntity<Map<String, Object>> deleteplan(@RequestBody PlanDeleteRequest planDeleteRequest) {
		return planDetailsService.deletePlan(planDeleteRequest);
	}

	@PostMapping("/plan/get")
	public ResponseEntity<Map<String, Object>> getPlan(@RequestBody PlanGetRequest planGetRequest) {
		return planDetailsService.getPlan(planGetRequest);
	}

	@PostMapping("/plan/checkexpiry")
	public ResponseEntity<Map<String, Object>> checkPlanExpiry(
			@RequestBody PlanCheckExpiryRequest planCheckExpiryRequest) {
		return planDetailsService.checkPlanExpiry(planCheckExpiryRequest);
	}

	@GetMapping("/user/category/list")
	public ResponseEntity<Map<String, Object>> userCategoryList(@RequestBody CategoryListRequest categoryListRequest) {
		return categoryDetailsService.userCategoryList(categoryListRequest);
	}

}
