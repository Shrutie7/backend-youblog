package com.youblog.services.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.PlanDetailsEntity;
import com.youblog.payloads.PlanCheckExpiryRequest;
import com.youblog.payloads.PlanCreateRequest;
import com.youblog.payloads.PlanDeleteRequest;
import com.youblog.payloads.PlanEditRequest;
import com.youblog.payloads.PlanGetRequest;
import com.youblog.payloads.PlanListRequest;
import com.youblog.repositories.PlanDetailsRepository;
import com.youblog.repositories.UserDetailsRepository;
import com.youblog.services.PlanDetailsService;
import com.youblog.utils.ResponseHandler;

@Service
public class PlanDetailsServiceImpl implements PlanDetailsService {

	@Autowired
	PlanDetailsRepository planDetailsRepository;

	@Autowired
	UserDetailsRepository userDetailsRepository;

	@Override
	public ResponseEntity<Map<String, Object>> createPlan(PlanCreateRequest planCreateRequest) {

		PlanDetailsEntity plan = new PlanDetailsEntity();
		plan.setPlanName(planCreateRequest.getPlanName());
		plan.setPlanDescription(planCreateRequest.getPlanDescription());
		plan.setPlanDuration(planCreateRequest.getPlanDuration());
		plan.setPlanPrice(planCreateRequest.getPlanPrice());
		plan.setGymTypeId(planCreateRequest.getGymTypeId());
		plan.setCategoryId(planCreateRequest.getCategoryId());
		plan.setActiveFlag(true);
		planDetailsRepository.save(plan);

		return ResponseHandler.response(null, "Plan created successfully", true);

	}

	@Override
	public ResponseEntity<Map<String, Object>> planList(PlanListRequest planListRequest) {

		if (planListRequest.getGymTypeId() != null) {
			ArrayList<Object[]> getplanlist = planDetailsRepository.getPlanList(planListRequest.getGymTypeId());
			ArrayList<Map<String, Object>> planList = new ArrayList<>();

			Map<String, Object> ListOfPlan = new HashMap<>();
			if (getplanlist.size() == 0) {

				ListOfPlan.put("ListOfPlans", planList);
				return ResponseHandler.response(ListOfPlan, "plan list cannot be found", false);

			} else {
				getplanlist.forEach(ele -> {
					Map<String, Object> res = new HashMap<>();
					res.put("planId", ele[0].toString() != null ? ele[0].toString() : "");
					res.put("PlanName", ele[1].toString() != null ? ele[1].toString() : "N/A");
					res.put("planDuration", ele[2].toString() != null ? ele[2].toString() : "");
					res.put("planPrice", ele[3].toString() != null ? ele[3].toString() : "");
					res.put("planDescription", ele[4].toString() != null ? ele[4].toString() : "N/A");
					planList.add(res);
				});
				ListOfPlan.put("ListOfPlans", planList);
				return ResponseHandler.response(ListOfPlan, "plan list found", true);
			}
		} else {
			return ResponseHandler.response(null, "Please provide gymTypeId", false);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> editPlan(PlanEditRequest planEditRequest) {

		if (planEditRequest.getPlanId() != null) {

//			Long checkuserplanmap =  planDetailsRepository.checkuserplanmapping(planEditRequest.getPlanId());

//			if(checkuserplanmap != null ) {
			PlanDetailsEntity getplandetails = planDetailsRepository.editPlan(planEditRequest.getPlanId());

			if (getplandetails == null) {
				return ResponseHandler.response(null, "plan cannot be edited", false);

			} else {
				getplandetails.setPlanName(planEditRequest.getPlanName());
				getplandetails.setPlanDescription(planEditRequest.getPlanDescription());
				getplandetails.setPlanDuration(planEditRequest.getPlanDuration());
				getplandetails.setPlanPrice(planEditRequest.getPlanPrice());
				getplandetails.setCategoryId(planEditRequest.getCategoryId());

				planDetailsRepository.save(getplandetails);

				return ResponseHandler.response(null, "plan updated successfully", true);
			}
//			}
//			else {
//				return ResponseHandler.response(null,"Plan cannot be updated since already mapped to some user", false);
//			}

		}

		else {
			return ResponseHandler.response(null, "Please provide planId", false);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> deletePlan(PlanDeleteRequest planDeleteRequest) {

		if (planDeleteRequest.getPlanId() != null) {

//			Long checkuserplanmap =  planDetailsRepository.checkuserplanmapping(planDeleteRequest.getPlanId());
//			System.out.println(checkuserplanmap);
//			if(checkuserplanmap != null ) {
			PlanDetailsEntity getplandetails = planDetailsRepository.deletePlan(planDeleteRequest.getPlanId());

			if (getplandetails == null) {
				return ResponseHandler.response(null, "plan cannot be deleted", false);

			} else {

				getplandetails.setActiveFlag(false);
				planDetailsRepository.save(getplandetails);

				return ResponseHandler.response(null, "plan deleted successfully", true);
			}
//			}
//			else {
//				return ResponseHandler.response(null,"Plan cannot be deleted since already mapped to some user", false);
//			}

		}

		else {
			return ResponseHandler.response(null, "Please provide planId", false);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> getPlan(PlanGetRequest planGetRequest) {

		if (planGetRequest.getPlanId() != null) {

			ArrayList<Object[]> getdetails = planDetailsRepository.getPlan(planGetRequest.getPlanId());

			if (getdetails != null) {

				Map<String, Object> hm = new HashMap<>();
				getdetails.forEach(ele -> {
					hm.put("planName", ele[0].toString() != null ? ele[0].toString() : "N/A");
					hm.put("planDuration", ele[1].toString() != null ? ele[1].toString() : "N/A");
					hm.put("planPrice", ele[2].toString() != null ? ele[2].toString() : "N/A");
					hm.put("planDescription", ele[3].toString() != null ? ele[3].toString() : "N/A");
					hm.put("categoryId", ele[4]);
					hm.put("gymTypeId", ele[5].toString() != null ? ele[5].toString() : "N/A");
				});
				return ResponseHandler.response(hm, "Plan details fetched", true);
			} else {
				return ResponseHandler.response(null, "Plan not found for given plan Id", false);
			}

		} else {
			return ResponseHandler.response(null, "Please provide Plan Id", false);

		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> checkPlanExpiry(PlanCheckExpiryRequest planCheckExpiryRequest) {

		if (planCheckExpiryRequest.getUserId() != null) {
			Boolean flag = false;

			Object[] getexpirydetails = planDetailsRepository.planExpiryCheck(planCheckExpiryRequest.getUserId());

			System.out.println(getexpirydetails);

			if (getexpirydetails == null) {
				return ResponseHandler.response(null, "plan expiry check failed", false);
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
				return ResponseHandler.response(flag, "plan expiry check successfull", true);
			}
		} else {
			return ResponseHandler.response(null, "please provide userId", false);
		}
	}
}
