package com.youblog.services.serviceimpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.PlanDetailsEntity;
import com.youblog.payloads.PlanCreateRequest;
import com.youblog.repositories.PlanDetailsRepository;
import com.youblog.services.PlanDetailsService;
import com.youblog.utils.ResponseHandler;


@Service
public class planDetailsServiceImpl implements PlanDetailsService{

	@Autowired
	PlanDetailsRepository plandetailsrepo;
	
	@Override
	public ResponseEntity<Map<String, Object>> createPlan(PlanCreateRequest usreq) {
		
		PlanDetailsEntity plan = new PlanDetailsEntity();

		
		
			plan.setPlanName(usreq.getPlanName());
			plan.setPlanDescription(usreq.getPlanDescription());
			plan.setPlanDuration(usreq.getPlanDuration());
			plan.setPlanPrice(usreq.getPlanPrice());
			plan.setGymTypeId(usreq.getGymTypeId());
			plan.setCategoryId(usreq.getCategoryId());
plan.setActiveFlag(true);
			plandetailsrepo.save(plan);

			return ResponseHandler.response(null, "Plan created successfully", true);
	
	
	}

}
