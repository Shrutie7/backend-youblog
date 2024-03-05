package com.youblog.services.serviceimpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.GymDetailsEntity;
import com.youblog.entities.ImageDetailsEntity;
import com.youblog.entities.UserDetailsEntity;
import com.youblog.entities.WorklistDetailsEntity;
import com.youblog.payloads.GetWorklistDetails;
import com.youblog.payloads.WorklistCreateRequest;
import com.youblog.payloads.WorklistUpdateRequest;
import com.youblog.repositories.GymDetailsRepository;
import com.youblog.repositories.ImageDetailsRepository;
import com.youblog.repositories.UserDetailsRepository;
import com.youblog.repositories.WorklistDetailsRepository;
import com.youblog.services.WorklistService;
import com.youblog.utils.DateParser;
import com.youblog.utils.KeycloakUtils;
import com.youblog.utils.ResponseHandler;

@Service
public class WorklistServiceImpl implements WorklistService {

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private GymDetailsRepository gymDetailsRepository;

	@Autowired
	private WorklistDetailsRepository worklistDetailsRepository;

	@Autowired
	private ImageDetailsRepository imageDetailsRepository;

	@Autowired
	private KeycloakUtils keycloakUtils;

	private static final int WORK_FLOW_MASTER_ID_USER_CANCEL_SUB = 1;

	public static final int WORK_FLOW_MASTER_ID_TRAINER_RESIGN = 4;

	public static final int WORK_FLOW_MASTER_ID_TRAINER_REGISTER = 5;

	public static final int WORK_FLOW_MASTER_ID_OWNER_REGISTER = 7;

	public static final int WORK_FLOW_MASTER_ID_USER_RELOCATE = 2;

	public static final int WORK_FLOW_MASTER_ID_TRAINER_RELOCATE = 6;

	public static final int WORK_FLOW_MASTER_ID_TRAINER_RELOCATE_OTHER = 9;

	@Override
	public ResponseEntity<Map<String, Object>> initiateWorkList(WorklistCreateRequest worklistCreateRequest) {
		WorklistDetailsEntity worklistDetails = new WorklistDetailsEntity();
		worklistDetails.setWorklistStatus("P");
		worklistDetails.setActionUserId(worklistCreateRequest.getActionUserId());
		worklistDetails.setInitiatedDate(Date.from(Instant.now()));
		worklistDetails.setInitiatedData(worklistCreateRequest.getInitiatedData());
		worklistDetails.setInitiatedUserId(worklistCreateRequest.getInitiatedUserId());
		worklistDetails.setWorkflowMasterId(worklistCreateRequest.getWorkflowMasterId());
		worklistDetailsRepository.save(worklistDetails);
		return ResponseHandler.response(null, "Request Initiated Successfully", true);
	}

	@Override
	public ResponseEntity<Map<String, Object>> updateWorkList(WorklistUpdateRequest worklistUpdateRequest) {
		WorklistDetailsEntity worklistDetails = worklistDetailsRepository
				.findPendingWorklist(worklistUpdateRequest.getWorklistDetailsId());
		if (worklistDetails == null) {
			return ResponseHandler.response(null, "No Worklist Details Found.", false);
		}
		Long workflowMasterId = worklistDetails.getWorkflowMasterId();
		String action = worklistUpdateRequest.getAction();
		boolean successFlag = false;
		UserDetailsEntity user = userDetailsRepository.findByUserId(worklistDetails.getInitiatedUserId());
		switch (workflowMasterId.intValue()) {
		case WORK_FLOW_MASTER_ID_USER_CANCEL_SUB:
			if (action.equals("Approve")) {
				user.setActiveFlag(false);
				keycloakUserDelete(worklistDetails.getInitiatedUserId());
				successFlag = true;
			}
			break;
		case WORK_FLOW_MASTER_ID_TRAINER_RESIGN:
			if (action.equals("Approve")) {
				user.setActiveFlag(false);
				boolean change = changeTrainerIds(worklistDetails.getInitiatedUserId());
				if (change) {
					keycloakUserDelete(worklistDetails.getInitiatedUserId());
					successFlag = true;
				}
			}
			break;
		case WORK_FLOW_MASTER_ID_TRAINER_REGISTER:
		case WORK_FLOW_MASTER_ID_OWNER_REGISTER:
			if (action.equals("Approve")) {
				user.setActiveFlag(true);
			}
			if (action.equals("Reject")) {
				keycloakUserDelete(worklistDetails.getInitiatedUserId());
			}
			successFlag = true;
			break;
		case WORK_FLOW_MASTER_ID_USER_RELOCATE:
			if (action.equals("Approve")) {
				successFlag = userRelocate(worklistDetails);
			}
			break;
		case WORK_FLOW_MASTER_ID_TRAINER_RELOCATE:
			if (action.equals("Approve")) {
				successFlag = trainerRelocate(worklistDetails);
			}
			break;
		case WORK_FLOW_MASTER_ID_TRAINER_RELOCATE_OTHER:
			if (action.equals("Approve")) {

			}
			break;
		}
		if (successFlag) {
			user.setWorklistStatus("C");
			userDetailsRepository.save(user);
			worklistDetails.setActedDate(Date.from(Instant.now()));
			worklistDetails.setWorklistStatus("C");
			worklistDetails.setActionTaken(action);
			worklistDetailsRepository.save(worklistDetails);
			return ResponseHandler.response(null, "Request Updated Successfully", true);
		} else {
			return ResponseHandler.response(null, "Failed to Updated Request", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> getPendingWorklist(GetWorklistDetails getPendingWorklist) {
		if (getPendingWorklist.getUserId() == null) {
			return ResponseHandler.response(null, "Please Provide User Id", false);
		}
		List<Object[]> worklistDetails = worklistDetailsRepository.getWorklistData("P", getPendingWorklist.getUserId());
		if (worklistDetails.isEmpty()) {
			return ResponseHandler.response(new ArrayList<>(), "No Pending Worklist Available", false);
		}
		Map<String, Object> response = worklistResponseConstructor(worklistDetails);
		return ResponseHandler.response(response, "Pending Worklist Details Fetched Successfully", true);
	}

	private Map<String, Object> worklistResponseConstructor(List<Object[]> worklistDetails) {
		JSONObject response = new JSONObject();
		worklistDetails.forEach(data -> {
			JSONObject subResponse = new JSONObject();
			JSONObject initiatedUserDetails = new JSONObject();
			subResponse.put("worklistDetailsId", data[0]);
			initiatedUserDetails.put("userId", data[1]);
			initiatedUserDetails.put("action", data[8] != null ? data[8] : "");
			initiatedUserDetails.put("userName", data[9] != " " ? data[9] : "");
			initiatedUserDetails.put("emailId", data[10]);
			initiatedUserDetails.put("roleId", data[11]);
			initiatedUserDetails.put("categoryId", data[12] != null ? data[12] : "");
			if (data[13] != null) {
				Optional<ImageDetailsEntity> image = imageDetailsRepository.findById(data[13].toString());
				if (!image.isEmpty()) {
					initiatedUserDetails.put("image", image.get().getImage() != null ? image.get().getImage() : "");
				} else {
					initiatedUserDetails.put("image", "");
				}
			} else {
				initiatedUserDetails.put("image", "");
			}
			boolean approveFlag = false;

			switch (Integer.valueOf(data[2].toString())) {
			case WORK_FLOW_MASTER_ID_USER_CANCEL_SUB:
			case WORK_FLOW_MASTER_ID_TRAINER_REGISTER:
			case WORK_FLOW_MASTER_ID_OWNER_REGISTER:
			case WORK_FLOW_MASTER_ID_USER_RELOCATE:
				approveFlag = true;
				break;
			case WORK_FLOW_MASTER_ID_TRAINER_RESIGN:
			case WORK_FLOW_MASTER_ID_TRAINER_RELOCATE:
				if (data[16] != null) {
					UserDetailsEntity user = userDetailsRepository.findByUserId(Long.valueOf(data[0].toString()));
					UserDetailsEntity newTrainer = userDetailsRepository
							.findCurrentTrainer(Long.valueOf(data[12].toString()), Long.valueOf(data[16].toString()));
					if (newTrainer != null) {
						if (user.getUserId() != newTrainer.getUserId()) {
							approveFlag = true;
						}
					}
				}
				break;
			}
			subResponse.put("approveFlag", approveFlag);
			subResponse.put("activeFlag", data[14]);
			subResponse.put("workflowName", data[15]);
			subResponse.put("initiatedBy", initiatedUserDetails);
			subResponse.put("workflowMasterId", data[2]);
			subResponse.put("actionUserId", data[3]);
			subResponse.put("initiatedData", data[4] != null ? new JSONObject(data[4].toString()) : "");
			subResponse.put("initiatedDate",
					data[5] != null ? DateParser.dateToString("dd MMM yy HH:mm", (Date) data[5]) : "");
			subResponse.put("actedDate",
					data[7] != null ? DateParser.dateToString("dd MMM yy HH:mm", (Date) data[7]) : "");
			subResponse.put("worklistStatus", data[6]);
			response.append("worklistData", subResponse);
		});
		return response.toMap();
	}

	@Override
	public ResponseEntity<Map<String, Object>> getCompletedWorklist(GetWorklistDetails getCompletedWorklist) {
		if (getCompletedWorklist.getUserId() == null) {
			return ResponseHandler.response(null, "Please Provide User Id", false);
		}
		List<Object[]> worklistDetails = worklistDetailsRepository.getWorklistData("C",
				getCompletedWorklist.getUserId());
		if (worklistDetails.isEmpty()) {
			return ResponseHandler.response(new ArrayList<>(), "No Completed Worklist Available", false);
		}
		Map<String, Object> response = worklistResponseConstructor(worklistDetails);
		return ResponseHandler.response(response, "Completed Worklist Details Fetched Successfully", true);
	}

	@Override
	public ResponseEntity<Map<String, Object>> getRequestedWorklist(GetWorklistDetails getRequestedWorklist) {
		if (getRequestedWorklist.getUserId() == null) {
			return ResponseHandler.response(null, "Please Provide User Id", false);
		}
		List<Object[]> worklistDetails = worklistDetailsRepository
				.getRequestedWorklistData(getRequestedWorklist.getUserId());
		if (worklistDetails.isEmpty()) {
			return ResponseHandler.response(new ArrayList<>(), "No Requested Worklist Available", false);
		}
		JSONObject response = new JSONObject();
		worklistDetails.forEach(data -> {
			JSONObject subResponse = new JSONObject();
			JSONObject initiatedUserDetails = new JSONObject();
			subResponse.put("worklistDetailsId", data[0]);
			initiatedUserDetails.put("userId", data[3]);
			initiatedUserDetails.put("userName", data[9] != " " ? data[9] : "");
			initiatedUserDetails.put("emailId", data[10]);
			initiatedUserDetails.put("roleId", data[11]);
			initiatedUserDetails.put("categoryId", data[12] != null ? data[12] : "");
			if (data[13] != null) {
				Optional<ImageDetailsEntity> image = imageDetailsRepository.findById(data[13].toString());
				if (!image.isEmpty()) {
					initiatedUserDetails.put("image", image.get().getImage() != null ? image.get().getImage() : "");
				} else {
					initiatedUserDetails.put("image", "");
				}
			} else {
				initiatedUserDetails.put("image", "");
			}
			subResponse.put("activeFlag", data[14]);
			subResponse.put("workflowName", data[15]);
			if (data[6].toString().equals("P")) {
				subResponse.put("pendingWith", initiatedUserDetails);
			} else {
				subResponse.put("pendingWith", new JSONObject());
			}
			if (data[6].toString().equals("C")) {
				subResponse.put("actedBy", initiatedUserDetails);
			} else {
				subResponse.put("actedBy", new JSONObject());
			}
			subResponse.put("workflowMasterId", data[2]);
			subResponse.put("action", data[8] != null ? data[8] : "");
			subResponse.put("initiatedUserId", data[1]);
			subResponse.put("initiatedData", data[4] != null ? new JSONObject(data[4].toString()) : "");
			subResponse.put("initiatedDate",
					data[5] != null ? DateParser.dateToString("dd MMM yy HH:mm", (Date) data[5]) : "");
			subResponse.put("actedDate",
					data[7] != null ? DateParser.dateToString("dd MMM yy HH:mm", (Date) data[7]) : "");
			subResponse.put("worklistStatus", data[6]);
			response.append("worklistData", subResponse);
		});
		return ResponseHandler.response(response.toMap(), "Requested Worklist Details Fetched Successfully", true);
	}

	public Long actionUserId(Long roleId, Long userId) {
		if (roleId == 2) {
			return 1L;
		}
		UserDetailsEntity user = userDetailsRepository.findByUserId(userId);
		if (user != null) {
			if (roleId == 3) {
				return user.getParentUserId();
			}
			if (roleId == 4) {
				GymDetailsEntity gymData = gymDetailsRepository.findByGymId(user.getGymId());
				if (gymData != null) {
					return gymData.getOwnerId();
				} else {
					return 0L;
				}
			}
		}
		return 0L;
	}

	private void keycloakUserDelete(Long userId) {
		UserDetailsEntity user = userDetailsRepository.findByUserId(userId);
		Map<String, String> token = keycloakUtils.getAdminToken();
		String accesstoken = token.get("access_token");
		keycloakUtils.keycloakUserDelete(user.getEmailId().trim(), accesstoken);
	}

	private boolean changeTrainerIds(Long userId) {
		UserDetailsEntity user = userDetailsRepository.findByUserId(userId);
		UserDetailsEntity newTrainer = userDetailsRepository.findNewTrainer(user.getCategoryId(), user.getGymId());
		if (newTrainer != null) {
			if (user.getUserId() != newTrainer.getUserId()) {
				List<UserDetailsEntity> users = userDetailsRepository.findUsersUnderTrainer(user.getUserId());
				List<UserDetailsEntity> normalUsers = new ArrayList<>();
				users.forEach(data -> {
					data.setParentUserId(newTrainer.getUserId());
					normalUsers.add(data);
				});
				userDetailsRepository.saveAll(normalUsers);
				return true;
			}
			return false;
		}
		return false;
	}

	private boolean userRelocate(WorklistDetailsEntity worklistDetails) {
		Map<String, Object> changeGymLocationRequest = worklistDetails.getInitiatedData();
		UserDetailsEntity user = userDetailsRepository.findByUserId(worklistDetails.getInitiatedUserId());
		if (user != null) {
			UserDetailsEntity trainer = userDetailsRepository.findByUserId(user.getParentUserId());
			if (trainer != null) {
				UserDetailsEntity newTrainer = userDetailsRepository.findNewTrainer(trainer.getCategoryId(),
						Long.valueOf(changeGymLocationRequest.get("gymId").toString()));
				if (newTrainer != null) {
					user.setParentUserId(newTrainer.getUserId());
					user.setGymId(Long.valueOf(changeGymLocationRequest.get("gymId").toString()));
					user.setLocationId(Long.valueOf(changeGymLocationRequest.get("locationId").toString()));
					userDetailsRepository.save(user);
					return true;
				}
			}
		}
		return false;
	}

	private boolean trainerRelocate(WorklistDetailsEntity worklistDetails) {
		Map<String, Object> changeGymLocationRequest = worklistDetails.getInitiatedData();
		UserDetailsEntity user = userDetailsRepository.findByUserId(worklistDetails.getInitiatedUserId());
		if (user != null) {
			UserDetailsEntity existing = userDetailsRepository.findCurrentTrainer(user.getCategoryId(),
					Long.valueOf(changeGymLocationRequest.get("gymId").toString()));
			if (existing != null) {
				List<UserDetailsEntity> users = userDetailsRepository.findUsersUnderTrainer(existing.getUserId());
				List<UserDetailsEntity> normalUsers = new ArrayList<>();
				users.forEach(data -> {
					data.setParentUserId(user.getUserId());
					normalUsers.add(data);
				});
				user.setParentUserId(gymDetailsRepository
						.findByGymId(Long.valueOf(changeGymLocationRequest.get("gymId").toString())).getOwnerId());
				user.setGymId(Long.valueOf(changeGymLocationRequest.get("gymId").toString()));
				user.setLocationId(Long.valueOf(changeGymLocationRequest.get("locationId").toString()));
				userDetailsRepository.save(user);
				userDetailsRepository.saveAll(normalUsers);
				return true;
			}
		}
		return false;
	}

}
