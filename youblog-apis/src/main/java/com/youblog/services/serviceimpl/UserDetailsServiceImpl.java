package com.youblog.services.serviceimpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.UserDetailsEntity;
import com.youblog.payloads.GetUserRequest;
import com.youblog.payloads.PlanPurchaseRequest;
import com.youblog.payloads.TrainerListRequest;
import com.youblog.payloads.UpdatePasswordRequest;
import com.youblog.payloads.UpdateUserRequest;
import com.youblog.payloads.UserDetailsRequest;
import com.youblog.repositories.UserDetailsRepository;
import com.youblog.services.UserDetailsService;
import com.youblog.utils.KeycloakUtils;
import com.youblog.utils.ResponseHandler;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserDetailsRepository userDetailsRepository;

	@Autowired
	KeycloakUtils keycloakUtilities;

	@Override
	public ResponseEntity<Map<String, Object>> createUser(UserDetailsRequest userDetailsRequest) {

		UserDetailsEntity checkflag = userDetailsRepository.checkEmail(userDetailsRequest.getEmailId());

		if (checkflag == null) {

			Map<String, String> token = keycloakUtilities.getAdminToken();
			String accesstoken = token.get("access_token");
			ResponseEntity<Map<String, Object>> flag = keycloakUtilities.keycloakUserCreator(
					userDetailsRequest.getEmailId(), userDetailsRequest.getPassword(),
					userDetailsRequest.getFirstName(), userDetailsRequest.getLastName(),
					userDetailsRequest.getEmailId(), true, accesstoken);

			if (!Boolean.valueOf(flag.getBody().get("status").toString())) {

				return ResponseHandler.response(null, flag.getBody().get("message").toString(), false);
			}
			try {
				UserDetailsEntity use = new UserDetailsEntity();
				use.setEmailId(userDetailsRequest.getEmailId().toLowerCase());
				use.setFirstName(userDetailsRequest.getFirstName());
				use.setLastName(userDetailsRequest.getLastName());
				use.setLocationId(userDetailsRequest.getLocationId());
				use.setParentUserId(userDetailsRequest.getParentUserId());
				use.setRoleId(userDetailsRequest.getRoleId());
				use.setUserName(userDetailsRequest.getUserName());
				use.setGender(userDetailsRequest.getGender());
				use.setActiveFlag(true);
				use.setCategoryId(userDetailsRequest.getCategoryId());
				use.setGymId(userDetailsRequest.getGymId());
				userDetailsRepository.save(use);
			} catch (Exception e) {
//				e.printStackTrace();
				keycloakUtilities.keycloakUserDelete(userDetailsRequest.getEmailId(), accesstoken);

				return ResponseHandler.response(null, "user cannot be created because " + e.getLocalizedMessage(),
						false);

			}
			return ResponseHandler.response(null, "created", true);
		} else {
			return ResponseHandler.response(null, "email already present", false);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> getUser(GetUserRequest getUserRequest) {
		// TODO Auto-generated method stub
		if (getUserRequest.getEmailId() != null) {
			ArrayList<Object[]> getdetails = userDetailsRepository
					.getUserDetails(getUserRequest.getEmailId().toLowerCase());
			System.out.println(getdetails);
			if (getdetails != null) {
				Map<String, Object> hm = new HashMap<>();
//				hm.put("firstName", getdetails.getFirstName());
//				hm.put("lastName",getdetails.getLastName());
//				hm.put("gender", getdetails.getGender());
//				hm.put("emailId", getdetails.getEmailId());
//				hm.put("userId", getdetails.getUserId());
//				hm.put("userName", getdetails.getUserName());
//				hm.put("roleId",getdetails.getRoleId());

				getdetails.forEach(ele -> {

					hm.put("userName", ele[0] != null ? ele[0].toString() : "N/A");
					hm.put("emailId", ele[1] != null ? ele[1].toString() : "N/A");
					hm.put("firstName", ele[2] != null ? ele[2].toString() : "N/A");
					hm.put("lastName", ele[3] != null ? ele[3].toString() : "N/A");
					hm.put("roleId", ele[4] != null ? ele[4].toString() : "");
					hm.put("gender", ele[5] != null ? ele[5].toString() : "N/A");
					hm.put("roleName", ele[6] != null ? ele[6].toString() : "N/A");
					hm.put("userId", ele[7] != null ? ele[7].toString() : "N/A");
					hm.put("gymId", ele[8] != null ? ele[8].toString() : "");

					Map<String, Object> parentdetails = new HashMap<>();

					if (ele[9] != null) {
						parentdetails.put("username", ele[10] != null ? ele[10].toString() : "N/A");
						parentdetails.put("firstName", ele[11] != null ? ele[11].toString() : "N/A");
						parentdetails.put("lastName", ele[12] != null ? ele[12].toString() : "N/A");
						parentdetails.put("roleId", ele[13] != null ? ele[13].toString() : "");

					}
					hm.put("parentUserDetails", parentdetails);
					hm.put("parentUserId", ele[9] != null ? ele[9].toString() : "");

					Map<String, Object> locationDetails = new HashMap<>();

					locationDetails.put("locationId", ele[14] != null ? ele[14].toString() : "");
					locationDetails.put("state", ele[15] != null ? ele[15].toString() : "N/A");
					locationDetails.put("city", ele[16] != null ? ele[16].toString() : "N/A");
					locationDetails.put("location", ele[17] != null ? ele[17].toString() : "N/A");

					hm.put("locationDetails", locationDetails);

					Map<String, Object> planDetails = new HashMap<>();

					planDetails.put("planId", ele[18] != null ? ele[18].toString() : "N/A");
					planDetails.put("planName", ele[19] != null ? ele[19].toString() : "N/A");
					planDetails.put("planStartDate", ele[20] != null ? ele[20].toString() : "N/A");

					hm.put("planDetails", planDetails);

					hm.put("gymtypeId", ele[21] != null ? ele[21] : "");
				});

				return ResponseHandler.response(hm, "user details fetched", true);
			} else {
				return ResponseHandler.response(null, "No such user found", false);
			}

		} else {
			return ResponseHandler.response(null, "Please provide emailId", false);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> updateUser(UpdateUserRequest updateUserRequest) {
		if (updateUserRequest.getUserId() != null) {
			UserDetailsEntity getdetails = userDetailsRepository.updateUserDetails(updateUserRequest.getUserId());

			if (getdetails != null) {
				getdetails.setFirstName(updateUserRequest.getFirstName());
				getdetails.setLastName(updateUserRequest.getLastName());
				getdetails.setGender(updateUserRequest.getGender());
				getdetails.setUserName(updateUserRequest.getUserName());
				getdetails.setParentUserId(updateUserRequest.getParentUserId());// post approval of worklist
				getdetails.setLocationId(updateUserRequest.getLocationId());// post approval of worklist
				getdetails.setActiveFlag(updateUserRequest.getActiveFlag()); // post approval of worklist
				getdetails.setGymId(updateUserRequest.getGymId());
				userDetailsRepository.save(getdetails);
				return ResponseHandler.response(null, "user updated successfully", true);
			} else {
				return ResponseHandler.response(null, "user cannot be updated", false);
			}

		} else {
			return ResponseHandler.response(null, "Please Provide userId", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> updateUserPassword(UpdatePasswordRequest updatePasswordRequest) {

		if (updatePasswordRequest.getEmailId() != null) {
			if (updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmNewPassword())) {
				boolean validate = keycloakUtilities.validatePassword(updatePasswordRequest.getEmailId(),
						updatePasswordRequest.getCurrentPassword());

				if (validate) {
					Map<String, String> token = keycloakUtilities.getAdminToken();
					String accesstoken = token.get("access_token");
					ResponseEntity<Map<String, Object>> keycloak = keycloakUtilities.updatePassword(
							updatePasswordRequest.getEmailId(), updatePasswordRequest.getNewPassword(), accesstoken);

					return ResponseHandler.response(null, keycloak.getBody().get("message").toString(),
							Boolean.valueOf(keycloak.getBody().get("status").toString()));
				} else {
					return ResponseHandler.response(null, "current password not matched", false);
				}
			} else {
				return ResponseHandler.response(null, "New password and confirm password should be same", false);
			}
		} else {
			return ResponseHandler.response(null, "Please Provide emailId", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> getUserList() {
		// TODO Auto-generated method stub
		ArrayList<UserDetailsEntity> getuserlist = userDetailsRepository.getUserList();
		ArrayList<Map<String, Object>> userlist = new ArrayList<>();

		if (getuserlist == null) {
			return ResponseHandler.response(new ArrayList<>(), "user list cannot be found", false);
		} else {
			getuserlist.forEach(ele -> {
				Map<String, Object> res = new HashMap<>();
				res.put("emailId", ele.getEmailId());
				res.put("firstName", ele.getFirstName());
				res.put("lastName", ele.getLastName());
				res.put("gender", ele.getGender());
				res.put("locationId", ele.getLocationId());
				res.put("parentUserId", ele.getParentUserId());
				res.put("roleId", ele.getRoleId());
				res.put("planId", ele.getPlanId());
				res.put("userName", ele.getUserName());
				userlist.add(res);
			});
			return ResponseHandler.response(userlist, "user list found", true);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> getTrainerList(TrainerListRequest trainerListRequest) {

		if (trainerListRequest.getOwnerId() != null) {

			ArrayList<Object[]> getTrainerList = userDetailsRepository.getTrainerList(trainerListRequest.getOwnerId());

			ArrayList<Map<String, Object>> trainerlist = new ArrayList<>();

			Map<String, Object> listOfTrainers = new HashMap<>();
			if (getTrainerList.size() == 0) {
				listOfTrainers.put("listOfTrainers", trainerlist);

				return ResponseHandler.response(listOfTrainers, "trainer list not found", false);
			} else {

				getTrainerList.forEach(ele -> {
					Map<String, Object> res = new HashMap<>();

					res.put("userId", ele[2] != null ? ele[2].toString() : "");

//					String fullName =  "";
//					fullName = ele.getFirstName() + " " + ele.getLastName();

					res.put("trainerName", ele[1] != null ? ele[1].toString() : "N/A");
					res.put("rating", ele[0] != null ? ele[0].toString() : "");
					res.put("categoryId", ele[3] != null ? ele[3].toString() : "");
					res.put("categoryName", ele[4] != null ? ele[4].toString() : "");
					trainerlist.add(res);

				});
				listOfTrainers.put("listOfTrainers", trainerlist);

				return ResponseHandler.response(listOfTrainers, "trainer list found", true);
			}

		} else {
			return ResponseHandler.response(null, "Please Provide owner Id", false);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> planPurchase(PlanPurchaseRequest planPurchaseRequest) {
		if (planPurchaseRequest.getUserId() != null || planPurchaseRequest.getPlanId() != null) {
			UserDetailsEntity getdetails = userDetailsRepository.planPurchase(planPurchaseRequest.getUserId());
			System.out.println(getdetails);
			if (getdetails != null) {
				getdetails.setPlanPurchasedDate(Date.from(Instant.now()));
				getdetails.setPlanId(planPurchaseRequest.getPlanId());
				userDetailsRepository.save(getdetails);
				return ResponseHandler.response(null, "plan purchased successfully", true);
			} else {
				return ResponseHandler.response(null, "plan cannot be purchased", false);
			}
		} else {
			return ResponseHandler.response(null, "Please Provide userId and planId also", false);
		}
	}

}
