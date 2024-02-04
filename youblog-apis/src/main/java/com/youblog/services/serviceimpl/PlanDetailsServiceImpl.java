package com.youblog.services.serviceimpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.PlanDetailsEntity;
import com.youblog.entities.UserDetailsEntity;
import com.youblog.payloads.PLanGetRequest;
import com.youblog.payloads.PlanCheckExpiryRequest;
import com.youblog.payloads.PlanCreateRequest;
import com.youblog.payloads.PlanDeleteRequest;
import com.youblog.payloads.PlanEditRequest;
import com.youblog.payloads.PlanListRequest;
import com.youblog.payloads.PlanPurchaseRequest;
import com.youblog.repositories.PlanDetailsRepository;
import com.youblog.repositories.UserDetailsRepository;
import com.youblog.services.PlanDetailsService;
import com.youblog.utils.ResponseHandler;


@Service
public class PlanDetailsServiceImpl implements PlanDetailsService{

	@Autowired
	PlanDetailsRepository plandetailsrepo;
	
	
	@Autowired
	UserDetailsRepository userdetailsrepo;
	
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
	
	
	@Override
	public ResponseEntity<Map<String, Object>> getplanlist(PlanListRequest usreq) {
		
		if(usreq.getGymTypeId()!=null) {
			ArrayList<Object[]> getplanlist = plandetailsrepo.getplanlist(usreq.getGymTypeId());
			ArrayList<Map<String,Object>> planList = new ArrayList<>();
			
			
			Map<String, Object> ListOfPlan = new HashMap<>();
			if(getplanlist.size()==0) {
				
				ListOfPlan.put("ListOfPlans", planList);
				return ResponseHandler.response(ListOfPlan ,"plan list cannot be found", false);

			}
			else {
				getplanlist.forEach(ele->{
					Map<String,Object> res = new HashMap<>();
					res.put("planId", ele[0].toString()!=null ? ele[0].toString():"");	
					res.put("PlanName", ele[1].toString()!=null?ele[1].toString():"N/A");
					res.put("planDuration", ele[2].toString()!=null ? ele[2].toString():"");
					res.put("planPrice", ele[3].toString()!=null?ele[3].toString():"");
					res.put("planDescription", ele[4].toString()!=null?ele[4].toString():"N/A");
					planList.add(res);
					});
				ListOfPlan.put("ListOfPlans", planList);
				return ResponseHandler.response(ListOfPlan, "plan list found", true);
			}
		}
		else {
			return ResponseHandler.response(null,"Please provide gymTypeId", false);
		}

	}


	@Override
	public ResponseEntity<Map<String, Object>> editPlan(PlanEditRequest usreq) {
		
		if(usreq.getPlanId()!=null) {
			
//			Long checkuserplanmap =  plandetailsrepo.checkuserplanmapping(usreq.getPlanId());
			
//			if(checkuserplanmap != null ) {
			PlanDetailsEntity getplandetails = plandetailsrepo.editplan(usreq.getPlanId());
			
			if(getplandetails==null) {
				return ResponseHandler.response(null,"plan cannot be edited", false);
				
			}
			else {
				getplandetails.setPlanName(usreq.getPlanName());
				getplandetails.setPlanDescription(usreq.getPlanDescription());
				getplandetails.setPlanDuration(usreq.getPlanDuration());
				getplandetails.setPlanPrice(usreq.getPlanPrice());
				getplandetails.setCategoryId(usreq.getCategoryId());
				
		
				plandetailsrepo.save(getplandetails);
				
				return ResponseHandler.response(null, "plan updated successfully", true);
			}
//			}
//			else {
//				return ResponseHandler.response(null,"Plan cannot be updated since already mapped to some user", false);
//			}

		}
		
		else {
			return ResponseHandler.response(null,"Please provide planId", false);
		}
		
	}
	
	@Override
	public ResponseEntity<Map<String, Object>> deletePlan(PlanDeleteRequest usreq) {
		
		
		if(usreq.getPlanId()!=null) {
			
//			Long checkuserplanmap =  plandetailsrepo.checkuserplanmapping(usreq.getPlanId());
//			System.out.println(checkuserplanmap);
//			if(checkuserplanmap != null ) {
		PlanDetailsEntity getplandetails = plandetailsrepo.deleteplan(usreq.getPlanId());
			
			if(getplandetails==null) {
				return ResponseHandler.response(null,"plan cannot be deleted", false);
				
			}
			else {
				
				getplandetails.setActiveFlag(false);
				plandetailsrepo.save(getplandetails);
				
				return ResponseHandler.response(null, "plan deleted successfully", true);
			}
//			}
//			else {
//				return ResponseHandler.response(null,"Plan cannot be deleted since already mapped to some user", false);
//			}

		}
		
		else {
			return ResponseHandler.response(null,"Please provide planId", false);
		}
		
	}


	@Override
	public ResponseEntity<Map<String, Object>> getplan(PLanGetRequest usreq) {
	
		
		if(usreq.getPlanId()!=null) {
			
			ArrayList<Object[]> getdetails = plandetailsrepo.getplan(usreq.getPlanId());
			
			if(getdetails!=null) {
		
				Map<String, Object> hm = new HashMap<>();
				getdetails.forEach(ele->{
					hm.put("planName",ele[0].toString()!=null ? ele[0].toString():"N/A");
					hm.put("planDuration",ele[1].toString()!=null?ele[1].toString():"N/A");
					hm.put("planPrice",ele[2].toString()!=null ? ele[2].toString():"N/A");
					hm.put("planDescription",ele[3].toString()!=null ? ele[3].toString():"N/A");
					hm.put("categoryId",ele[4]);
					hm.put("gymTypeId",ele[5].toString()!=null ? ele[5].toString():"N/A");
				});

				
				return ResponseHandler.response(hm,"Plan details fetched", true);
			}else {
				return ResponseHandler.response(null,"Plan not found for given plan Id", false);
			}

		}
		else {
			return ResponseHandler.response(null,"Please provide Plan Id", false);
			
		}
	}


	@Override
	public ResponseEntity<Map<String, Object>> planexpirycheck(PlanCheckExpiryRequest usreq) {
		
	
		if(usreq.getUserId()!=null) {
			Boolean flag = false; 
			
		Object[] getexpirydetails = plandetailsrepo.planexpirycheck(usreq.getUserId());

		System.out.println(getexpirydetails);
		
			if(getexpirydetails==null) {
				return ResponseHandler.response(null,"plan expiry check failed", false);
			}
			
			else {
				
//				import java.time.LocalDate;
//				import java.time.format.DateTimeFormatter;
//
//				public class DateCalculator {
//				    public static void main(String[] args) {
//				        // Get today's date
//				        LocalDate currentDate = LocalDate.now();
//
//				        // Assuming you have a given month (as an integer)
//				        int givenMonth = 3;  // Replace this with your desired month
//
//				        // Add the given month to the current date
//				        LocalDate newDate = currentDate.plusMonths(givenMonth);
//
//				        // Display the results
//				        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//				        System.out.println("Current Date: " + formatter.format(currentDate));
//				        System.out.println("New Date after adding " + givenMonth + " months: " + formatter.format(newDate));
//				    }
//				}				
				return ResponseHandler.response(flag,"plan expiry check successfull", true);
			}
		
			
		}
		else {
			return ResponseHandler.response(null,"please provide userId", false);
		}
	}



	
	

}
