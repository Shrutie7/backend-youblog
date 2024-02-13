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

import com.youblog.payloads.FeedbackCreateRequest;
import com.youblog.payloads.FeedbackListRequest;
import com.youblog.payloads.GymCreateRequest;
import com.youblog.payloads.PLanGetRequest;
import com.youblog.payloads.PlanCheckExpiryRequest;
import com.youblog.payloads.PlanCreateRequest;
import com.youblog.payloads.PlanDeleteRequest;
import com.youblog.payloads.PlanEditRequest;
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
	private CategoryDetailsService categorydetailsservice;
	
	@Autowired
	private GymDetailsService gymdetailsservice;
	
	@Autowired
	private RoleDetailsService roledetailsservice;
	
	@Autowired
	private FeedbackService feedbackservice;
	
	@Autowired
	private PlanDetailsService plandetailsservice;
	
	@GetMapping("/categorylist")
	public ResponseEntity<Map<String,Object>> categoryList(){
		return categorydetailsservice.getCategoryList();
	}
		

	
	@GetMapping("/rolelist")
	public ResponseEntity<Map<String,Object>> roleList(){
		return roledetailsservice.getRoleList();
	}

	
	@PostMapping("/gymcreate")
	public ResponseEntity<Map<String,Object>> creategym(@RequestBody GymCreateRequest usreq){
		return gymdetailsservice.creategym(usreq);
	}
	
	@PostMapping("/feedbackcreate")
	public ResponseEntity<Map<String,Object>> feedbackcreate(@RequestBody FeedbackCreateRequest usreq){
		return feedbackservice.createFeedback(usreq);
	}
	
	@PostMapping("/feedback/list")
	public ResponseEntity<Map<String,Object>> feedbacklist(@RequestBody FeedbackListRequest usreq){
		return feedbackservice.getfeedbacklist(usreq);
	}
	
	@PostMapping("/plan/create")
	public ResponseEntity<Map<String,Object>> createplan(@RequestBody PlanCreateRequest usreq){
		return plandetailsservice.createPlan(usreq);
	}
	
	@PostMapping("/plan/list")
	public ResponseEntity<Map<String,Object>> planlist(@RequestBody PlanListRequest usreq){
		return plandetailsservice.getplanlist(usreq);
	}
	
	@PostMapping("/plan/edit")
	public ResponseEntity<Map<String,Object>> editplan(@RequestBody PlanEditRequest usreq){
		return plandetailsservice.editPlan(usreq);
	}
	
	@PostMapping("/plan/delete")
	public ResponseEntity<Map<String,Object>> deleteplan(@RequestBody PlanDeleteRequest usreq){
		return plandetailsservice.deletePlan(usreq);
	}
	
	@PostMapping("/plan/get")
	public ResponseEntity<Map<String,Object>> getplan(@RequestBody PLanGetRequest usreq){
		return plandetailsservice.getplan(usreq);
	}
	
	@PostMapping("/plan/checkexpiry")
	public ResponseEntity<Map<String,Object>> planexpirycheck(@RequestBody PlanCheckExpiryRequest usreq){
		return plandetailsservice.planexpirycheck(usreq);
	}
	

	
}
