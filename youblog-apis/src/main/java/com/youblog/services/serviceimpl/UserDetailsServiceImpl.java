package com.youblog.services.serviceimpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.ImageDetailsEntity;
import com.youblog.entities.UserDetailsEntity;
import com.youblog.payloads.CancelMembershipRequest;
import com.youblog.payloads.ChangeGymLocationRequest;
import com.youblog.payloads.GetUserRequest;
import com.youblog.payloads.PlanPurchaseRequest;
import com.youblog.payloads.TrainerListRequest;
import com.youblog.payloads.UpdatePasswordRequest;
import com.youblog.payloads.UpdateUserRequest;
import com.youblog.payloads.UserDetailsRequest;
import com.youblog.payloads.WorklistCreateRequest;
import com.youblog.repositories.GymDetailsRepository;
import com.youblog.repositories.ImageDetailsRepository;
import com.youblog.repositories.UserDetailsRepository;
import com.youblog.repositories.WorklistDetailsRepository;
import com.youblog.services.UserDetailsService;
import com.youblog.services.WorklistService;
import com.youblog.utils.DateParser;
import com.youblog.utils.KeycloakUtils;
import com.youblog.utils.ResponseHandler;
import com.youblog.utils.WorklistConstants;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserDetailsRepository userDetailsRepository;

	@Autowired
	KeycloakUtils keycloakUtilities;

	@Autowired
	ImageDetailsRepository imageDetailsRepository;

	@Autowired
	private WorklistService worklistService;

	@Autowired
	private WorklistServiceImpl worklistServiceImpl;

	@Autowired
	private WorklistDetailsRepository worklistDetailsRepository;

	@Autowired
	private GymDetailsRepository gymDetailsRepository;

	private static final Long WORK_FLOW_MASTER_ID_USER_CANCEL_SUB = Long
			.valueOf(WorklistConstants.WORK_FLOW_MASTER_ID_USER_CANCEL_SUB);

	public static final Long WORK_FLOW_MASTER_ID_TRAINER_RESIGN = Long
			.valueOf(WorklistConstants.WORK_FLOW_MASTER_ID_TRAINER_RESIGN);

	public static final Long WORK_FLOW_MASTER_ID_TRAINER_REGISTER = Long
			.valueOf(WorklistConstants.WORK_FLOW_MASTER_ID_TRAINER_REGISTER);

	public static final Long WORK_FLOW_MASTER_ID_OWNER_REGISTER = Long
			.valueOf(WorklistConstants.WORK_FLOW_MASTER_ID_OWNER_REGISTER);

	public static final Long WORK_FLOW_MASTER_ID_USER_RELOCATE = Long
			.valueOf(WorklistConstants.WORK_FLOW_MASTER_ID_USER_RELOCATE);

	public static final Long WORK_FLOW_MASTER_ID_TRAINER_RELOCATE = Long
			.valueOf(WorklistConstants.WORK_FLOW_MASTER_ID_TRAINER_RELOCATE);

	public static final Long WORK_FLOW_MASTER_ID_TRAINER_RELOCATE_OTHER = Long
			.valueOf(WorklistConstants.WORK_FLOW_MASTER_ID_TRAINER_RELOCATE_OTHER);

	@Override
	public ResponseEntity<Map<String, Object>> createUser(UserDetailsRequest userDetailsRequest) {

		UserDetailsEntity checkflag = userDetailsRepository.checkEmail(userDetailsRequest.getEmailId());

		if (checkflag == null) {

			Map<String, String> token = keycloakUtilities.getAdminToken();
			String accesstoken = token.get("access_token");
			ResponseEntity<Map<String, Object>> flag = keycloakUtilities.keycloakUserCreator(
					userDetailsRequest.getEmailId(), userDetailsRequest.getPassword(),
					userDetailsRequest.getFirstName().trim(), userDetailsRequest.getLastName().trim(),
					userDetailsRequest.getEmailId().trim(), true, accesstoken);

			if (!Boolean.valueOf(flag.getBody().get("status").toString())) {

				return ResponseHandler.response(null, flag.getBody().get("message").toString(), false);
			}
			try {
				UserDetailsEntity use = new UserDetailsEntity();
				use.setEmailId(userDetailsRequest.getEmailId().toLowerCase().trim());
				use.setFirstName(userDetailsRequest.getFirstName().trim());
				use.setLastName(userDetailsRequest.getLastName().trim());
				use.setLocationId(userDetailsRequest.getLocationId());
				use.setParentUserId(userDetailsRequest.getParentUserId());
				use.setRoleId(userDetailsRequest.getRoleId());
				use.setUserName(userDetailsRequest.getUserName().trim());
				use.setGender(userDetailsRequest.getGender());
				if (userDetailsRequest.getRoleId() == 4) {
					use.setActiveFlag(true);
				} else {
					use.setActiveFlag(false);
				}
				use.setCategoryId(userDetailsRequest.getCategoryId());
				use.setGymId(userDetailsRequest.getGymId());
				use.setWorklistStatus("P");
				UserDetailsEntity user = userDetailsRepository.save(use);
				if (userDetailsRequest.getRoleId() == 3 || userDetailsRequest.getRoleId() == 2) {
					WorklistCreateRequest worklistRequest = new WorklistCreateRequest();
					worklistRequest.setInitiatedUserId(user.getUserId());
					worklistRequest.setWorkflowMasterId(
							userDetailsRequest.getRoleId() == 2 ? WORK_FLOW_MASTER_ID_OWNER_REGISTER
									: WORK_FLOW_MASTER_ID_TRAINER_REGISTER);
					worklistRequest
							.setActionUserId(worklistServiceImpl.actionUserId(user.getRoleId(), user.getUserId()));
					return worklistService.initiateWorkList(worklistRequest);
				}
			} catch (Exception e) {
				keycloakUtilities.keycloakUserDelete(userDetailsRequest.getEmailId().trim(), accesstoken);
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
					hm.put("worklistStatus", ele[25] != null ? ele[25] : "C");
					hm.put("activeFlag", ele[26]);
					if (ele[24] != null) {
						Optional<ImageDetailsEntity> image = imageDetailsRepository.findById(ele[24].toString());
						if (!image.isEmpty()) {
							hm.put("image", image.get().getImage() != null ? image.get().getImage() : "");
						} else {
							hm.put("image", "");
						}
					} else {
						hm.put("image", "");
					}
					JSONObject worklist = new JSONObject();
					if (ele[25] != null && ele[25].toString().equals("P") && !Boolean.valueOf(ele[26].toString())) {
						List<Object[]> worklistDetails = worklistDetailsRepository.getRequestUserWorklistData("P",
								Long.valueOf(ele[7].toString()),
								Integer.valueOf(ele[4].toString()) == 2 ? WORK_FLOW_MASTER_ID_OWNER_REGISTER
										: WORK_FLOW_MASTER_ID_TRAINER_REGISTER);
						worklistDetails.forEach(data -> {
							JSONObject initiatedUserDetails = new JSONObject();
							worklist.put("worklistDetailsId", data[0]);
							initiatedUserDetails.put("userId", data[3]);
							initiatedUserDetails.put("userName", data[8] != " " ? data[8] : "");
							initiatedUserDetails.put("emailId", data[9]);
							initiatedUserDetails.put("roleId", data[10]);
							initiatedUserDetails.put("categoryId", data[11] != null ? data[11] : "");
							initiatedUserDetails.put("activeFlag", data[13]);
							if (data[12] != null) {
								Optional<ImageDetailsEntity> image = imageDetailsRepository
										.findById(data[12].toString());
								if (!image.isEmpty()) {
									initiatedUserDetails.put("image",
											image.get().getImage() != null ? image.get().getImage() : "");
								} else {
									initiatedUserDetails.put("image", "");
								}
							} else {
								initiatedUserDetails.put("image", "");
							}
							worklist.put("pendingWith", initiatedUserDetails);
							worklist.put("workflowMasterId", data[2]);
							worklist.put("initiatedUserId", data[1]);
							worklist.put("initiatedData", data[4] != null ? new JSONObject(data[4].toString()) : "");
							worklist.put("initiatedDate",
									data[5] != null ? DateParser.dateToString("dd MMM yy HH:mm", (Date) data[5]) : "");
							worklist.put("actedDate",
									data[7] != null ? DateParser.dateToString("dd MMM yy HH:mm", (Date) data[7]) : "");
							worklist.put("worklistStatus", data[6]);
						});
					}
					hm.put("worklistData", worklist.toMap());

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
					planDetails.put("planEndDate", ele[23] != null ? ele[23].toString() : "N/A");
					planDetails.put("planDuration", ele[22] != null ? ele[22].toString() : "N/A");
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
				getdetails.setFirstName(updateUserRequest.getFirstName().trim());
				getdetails.setLastName(updateUserRequest.getLastName().trim());
				getdetails.setGender(updateUserRequest.getGender());
				getdetails.setUserName(updateUserRequest.getUserName());
				getdetails.setParentUserId(updateUserRequest.getParentUserId());// post approval of worklist
				getdetails.setLocationId(updateUserRequest.getLocationId());// post approval of worklist
				getdetails.setActiveFlag(updateUserRequest.getActiveFlag()); // post approval of worklist
				getdetails.setGymId(updateUserRequest.getGymId());
				if (updateUserRequest.getImage() != null) {
					if (getdetails.getImageId() == null) {
						ImageDetailsEntity imageDetails = new ImageDetailsEntity();
						imageDetails.setTitle(getdetails.getEmailId());
						imageDetails.setImage(updateUserRequest.getImage());
						ImageDetailsEntity image = imageDetailsRepository.save(imageDetails);
						getdetails.setImageId(image.getId());
					} else {
						Optional<ImageDetailsEntity> image = imageDetailsRepository.findById(getdetails.getImageId());
						if (!image.isEmpty()) {
							ImageDetailsEntity img = image.get();
							img.setImage(updateUserRequest.getImage());
							imageDetailsRepository.save(img);
						}
					}
				}
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
				boolean validate = keycloakUtilities.validatePassword(updatePasswordRequest.getEmailId().trim(),
						updatePasswordRequest.getCurrentPassword());

				if (validate) {
					Map<String, String> token = keycloakUtilities.getAdminToken();
					String accesstoken = token.get("access_token");
					ResponseEntity<Map<String, Object>> keycloak = keycloakUtilities.updatePassword(
							updatePasswordRequest.getEmailId().trim(), updatePasswordRequest.getNewPassword(),
							accesstoken);

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

		if (trainerListRequest.getGymId() != null) {

			ArrayList<Object[]> getTrainerList = userDetailsRepository.getTrainerList(trainerListRequest.getGymId());

			ArrayList<Map<String, Object>> trainerlist = new ArrayList<>();

			Map<String, Object> listOfTrainers = new HashMap<>();
			if (getTrainerList.size() == 0) {
				listOfTrainers.put("listOfTrainers", trainerlist);

				return ResponseHandler.response(listOfTrainers, "trainer list not found", false);
			} else {

				getTrainerList.forEach(ele -> {
					Map<String, Object> res = new HashMap<>();
					res.put("userId", ele[2] != null ? ele[2].toString() : "");
					res.put("trainerName", ele[1] != null ? ele[1].toString() : "N/A");
					res.put("rating", ele[0] != null ? ele[0].toString() : 0);
					res.put("categoryId", ele[3] != null ? ele[3].toString() : "");
					res.put("categoryName", ele[4] != null ? ele[4].toString() : "");
					if (ele[5] != null) {
						Optional<ImageDetailsEntity> image = imageDetailsRepository.findById(ele[5].toString());
						if (!image.isEmpty()) {
							res.put("image", image.get().getImage() != null ? image.get().getImage() : "");
						} else {
							res.put("image", "");
						}
					} else {
						res.put("image", "");
					}
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
			UserDetailsEntity getdetails = userDetailsRepository.findByUserId(planPurchaseRequest.getUserId());
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

	@Override
	public ResponseEntity<Map<String, Object>> cancelMembership(CancelMembershipRequest cancelMembershipRequest) {
		if (cancelMembershipRequest.getUserId() != null) {
			UserDetailsEntity user = userDetailsRepository.findByUserId(cancelMembershipRequest.getUserId());
			user.setWorklistStatus("P");
			WorklistCreateRequest worklistRequest = new WorklistCreateRequest();
			worklistRequest.setActionUserId(worklistServiceImpl.actionUserId(cancelMembershipRequest.getRoleId(),
					cancelMembershipRequest.getUserId()));
			worklistRequest.setInitiatedData(cancelMembershipRequest.toMap());
			worklistRequest.setInitiatedUserId(cancelMembershipRequest.getUserId());
			worklistRequest
					.setWorkflowMasterId(cancelMembershipRequest.getRoleId() == 4 ? WORK_FLOW_MASTER_ID_USER_CANCEL_SUB
							: WORK_FLOW_MASTER_ID_TRAINER_RESIGN);
			userDetailsRepository.save(user);
			return worklistService.initiateWorkList(worklistRequest);
		} else {
			return ResponseHandler.response(null, "Please Provide User Id", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> changeGymLocation(ChangeGymLocationRequest changeGymLocationRequest) {
		if (changeGymLocationRequest.getUserId() != null) {
			UserDetailsEntity user = userDetailsRepository.findByUserId(changeGymLocationRequest.getUserId());
			user.setWorklistStatus("P");
			WorklistCreateRequest worklistRequest = new WorklistCreateRequest();
			worklistRequest.setActionUserId(worklistServiceImpl.actionUserId(changeGymLocationRequest.getRoleId(),
					changeGymLocationRequest.getUserId()));
			worklistRequest.setInitiatedData(changeGymLocationRequest.toMap());
			worklistRequest.setInitiatedUserId(changeGymLocationRequest.getUserId());
			worklistRequest
					.setWorkflowMasterId(changeGymLocationRequest.getRoleId() == 4 ? WORK_FLOW_MASTER_ID_USER_RELOCATE
							: WORK_FLOW_MASTER_ID_TRAINER_RELOCATE);
//			if (changeGymLocationRequest.getRoleId() == 3) {
//				WorklistCreateRequest worklistRequest1 = new WorklistCreateRequest();
//				worklistRequest1.setActionUserId(
//						gymDetailsRepository.findByGymId(changeGymLocationRequest.getGymId()).getOwnerId());
//				worklistRequest1.setInitiatedData(changeGymLocationRequest.toMap());
//				worklistRequest1.setInitiatedUserId(changeGymLocationRequest.getUserId());
//				worklistRequest1.setWorkflowMasterId(WORK_FLOW_MASTER_ID_TRAINER_RELOCATE_OTHER);
//				worklistService.initiateWorkList(worklistRequest1);
//			}
			userDetailsRepository.save(user);
			return worklistService.initiateWorkList(worklistRequest);
		} else {
			return ResponseHandler.response(null, "Please Provide User Id", false);
		}
	}

}
