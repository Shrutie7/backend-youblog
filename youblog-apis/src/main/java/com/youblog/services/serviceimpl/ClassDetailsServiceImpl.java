package com.youblog.services.serviceimpl;

import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.youblog.entities.ClassDetailsEntity;
import com.youblog.entities.ClassMasterEntity;
import com.youblog.entities.TimeDetailsEntity;
import com.youblog.entities.UserClassMappingEntity;
import com.youblog.payloads.ClassDetailsCreateRequest;
import com.youblog.payloads.ClassDetailsGetRequest;
import com.youblog.payloads.ClassDetailsListRequest;
import com.youblog.payloads.ClassDetailsListTrainerRequest;
import com.youblog.payloads.ClassDetailsUpdateRequest;
import com.youblog.payloads.ClassMasterCreateRequest;
import com.youblog.payloads.ClassMasterDeleteRequest;
import com.youblog.payloads.ClassUserLeaveRequest;
import com.youblog.payloads.ClassUserMappingRequest;
import com.youblog.repositories.ClassDetailsRepository;
import com.youblog.repositories.ClassMasterRepository;
import com.youblog.repositories.TimeDetailsRepository;
import com.youblog.repositories.UserClassMappingRepository;
import com.youblog.services.ClassDetailsService;
import com.youblog.utils.DateParser;
import com.youblog.utils.QueryExec;
import com.youblog.utils.ResponseHandler;
import com.youblog.utils.SqlCustomException;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClassDetailsServiceImpl implements ClassDetailsService {

	@Autowired
	private ClassMasterRepository classMasterRepository;

	@Autowired
	private TimeDetailsRepository timeDetailsRepository;

	@Autowired
	private ClassDetailsRepository classDetailsRepository;

	@Autowired
	private QueryExec queryExec;

	@Autowired
	private UserClassMappingRepository userClassMappingRepository;

	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> classMasterCreate(ClassMasterCreateRequest classMasterCreateRequest) {
		List<ClassMasterEntity> classes = new ArrayList<>();
		if (!classMasterCreateRequest.getClassList().isEmpty()) {
			classMasterCreateRequest.getClassList().forEach(master -> {
				ClassMasterEntity classMaster = new ClassMasterEntity();
				classMaster.setActiveFlag(true);
				classMaster.setClassName(master.getClassName());
				classes.add(classMaster);
			});
			classMasterRepository.saveAll(classes);
			return ResponseHandler.response(null, "Classes Created Successfully", true);
		}
		return ResponseHandler.response(null, "Please Provide Classes Data", false);
	}

	@Override
	public ResponseEntity<Map<String, Object>> classMasterList() {
		List<ClassMasterEntity> classList = classMasterRepository.classMasterList();
		if (!classList.isEmpty()) {
			JSONObject response = new JSONObject();
			classList.forEach(data -> {
				JSONObject subResponse = new JSONObject();
				subResponse.put("classId", data.getClassMasterId());
				subResponse.put("className", data.getClassName());
				response.append("classList", subResponse);
			});
			return ResponseHandler.response(response.toMap(), "Classes List Fetched Successfully", true);
		}
		return ResponseHandler.response(null, "No Classes Data Found", false);
	}

	@Override
	public ResponseEntity<Map<String, Object>> classMasterDelete(ClassMasterDeleteRequest classMasterDeleteRequest) {
		Optional<ClassMasterEntity> deleteClass = classMasterRepository
				.findById(classMasterDeleteRequest.getClassMasterId());
		if (deleteClass.isEmpty()) {
			return ResponseHandler.response(null, "Class Not Found", false);
		}
		ClassMasterEntity delete = deleteClass.get();
		delete.setActiveFlag(false);
		List<ClassDetailsEntity> classDetails = classDetailsRepository
				.findActiveClassesBasedOnClassMaster(classMasterDeleteRequest.getClassMasterId());
		List<ClassDetailsEntity> deleteClasses = new ArrayList<>();
		if (classDetails.size() > 0) {
			classDetails.forEach(data -> {
				data.setActiveFlag(false);
				try {
					deleteClassForAllUsers(data.getClassDetailsId());
				} catch (SqlCustomException e) {

				}
				deleteClasses.add(data);
			});
			classDetailsRepository.saveAll(deleteClasses);
		}
		classMasterRepository.save(delete);
		return ResponseHandler.response(null, "Class Deleted Successfully", true);
	}

	@Override
	public ResponseEntity<Map<String, Object>> getTimeDetails() {
		List<TimeDetailsEntity> timeList = timeDetailsRepository.getTimeDetails();
		if (!timeList.isEmpty()) {
			JSONObject response = new JSONObject();
			timeList.forEach(data -> {
				JSONObject subResponse = new JSONObject();
				subResponse.put("timeDetailsId", data.getTimeDetailsId());
				subResponse.put("timings", (data.getStartTime() < 12 ? data.getStartTime() + "AM"
						: data.getStartTime() == 12 ? data.getStartTime() + "PM" : data.getStartTime() - 12 + "PM")
						+ " - " + (data.getEndTime() < 12 ? data.getEndTime() + "AM"
								: data.getEndTime() == 12 ? data.getEndTime() + "PM" : data.getEndTime() - 12 + "PM"));
				response.append("timeList", subResponse);
			});
			return ResponseHandler.response(response.toMap(), "Time List Fetched Successfully", true);
		}
		return ResponseHandler.response(null, "No Timings Available", false);

	}

	@Override
	public ResponseEntity<Map<String, Object>> classDetailsCreate(ClassDetailsCreateRequest classDetailsCreateRequest) {

		Boolean checkClass = classDetailsRepository.checkTiming(classDetailsCreateRequest.getTrainerId(),
				classDetailsCreateRequest.getTimeDetailsId(), classDetailsCreateRequest.getWeekDay().toLowerCase());
		if (checkClass) {
			int checkCount = classDetailsRepository.checkCount(classDetailsCreateRequest.getTrainerId(),
					classDetailsCreateRequest.getWeekDay().toLowerCase());
			if (checkCount < 2) {
				ClassDetailsEntity classDetails = new ClassDetailsEntity();
				classDetails.setActiveFlag(true);
				classDetails.setWeekDay(classDetailsCreateRequest.getWeekDay().toLowerCase());
				classDetails.setGymId(classDetailsCreateRequest.getGymId());
				classDetails.setClassMasterId(classDetailsCreateRequest.getClassMasterId());
				classDetails.setTempCancelFlag(false);
				classDetails.setTrainerId(classDetailsCreateRequest.getTrainerId());
				classDetails.setTempChangeFlag(false);
				classDetails.setTimeDetailsId(classDetailsCreateRequest.getTimeDetailsId());
				try {
					classDetails
							.setStartDate(DateParser.dateParser("dd MMM yy", classDetailsCreateRequest.getStartDate()));
					classDetails.setEndDate(DateParser.dateParser("dd MMM yy HH:mm:ss",
							classDetailsCreateRequest.getEndDate() + " 23:59:59"));
				} catch (ParseException e) {
					return ResponseHandler.response(null, "Please Provide Valid Date Format", false);
				}
				classDetailsRepository.save(classDetails);
				return ResponseHandler.response(null, "Class Created Successfully", true);
			} else {
				return ResponseHandler.response(null,
						"Already 2 classes assigned for the day. Can't able to add class!", false);
			}
		} else {
			return ResponseHandler.response(null, "Class is already assigned for entered timings", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> classDetailsList(ClassDetailsListRequest classDetailsListRequest) {
		Date currentDate = Date.from(Instant.now());
		String response = classDetailsRepository.classDetailsList(classDetailsListRequest.getGymId(), currentDate);
		JSONObject responseObject = new JSONObject(response);
		JSONArray arr = responseObject.getJSONArray("classList");
		if (!arr.isEmpty()) {
			return ResponseHandler.response(responseObject.toMap(), "Class List Details Fetched Successfully", true);
		} else {
			return ResponseHandler.response(responseObject.toMap(), "Class List Not Found", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> classDetailsListTrainer(
			ClassDetailsListTrainerRequest classDetailsListTrainerRequest) {
		Date currentDate = Date.from(Instant.now());
		String response = classDetailsRepository.classDetailsListTrainer(classDetailsListTrainerRequest.getTrainerId(),
				currentDate);
		JSONObject responseObject = new JSONObject(response);
		JSONArray arr = responseObject.getJSONArray("classListTrainer");
		if (!arr.isEmpty()) {
			return ResponseHandler.response(responseObject.toMap(),
					"Class List Details For Trainer Fetched Successfully", true);
		} else {
			return ResponseHandler.response(responseObject.toMap(), "Class List Details For Trainer Not Found", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> classDetailsGet(ClassDetailsGetRequest classDetailsGetRequest) {
		String response = classDetailsRepository.classDetailsGet(classDetailsGetRequest.getClassDetailsId());
		JSONObject responseObject = new JSONObject();
		if (response != null) {
			responseObject = new JSONObject(response);
			return ResponseHandler.response(responseObject.toMap(), "Class Details Fetched Successfully", true);
		} else {
			return ResponseHandler.response(responseObject.toMap(), "Class Details Not Found", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> classDetailsUpdate(ClassDetailsUpdateRequest classDetailsUpdateRequest) {
		if (classDetailsUpdateRequest.getClassDetailsId() != null) {
			ClassDetailsEntity classDetails = classDetailsRepository
					.findActiveClassById(classDetailsUpdateRequest.getClassDetailsId());
			if (classDetails != null) {
				if (classDetailsUpdateRequest.getActiveFlag() != null) {
					classDetails.setActiveFlag(classDetailsUpdateRequest.getActiveFlag());
				}
				if (classDetailsUpdateRequest.getTempCancelFlag() != null) {
					classDetails.setTempCancelFlag(classDetailsUpdateRequest.getTempCancelFlag());
				}
				if (classDetailsUpdateRequest.getTempChangeFlag() != null
						&& classDetailsUpdateRequest.getTempTimeId() != null) {
					classDetails.setTempChangeFlag(classDetailsUpdateRequest.getTempChangeFlag());
					classDetails.setTempTimeId(classDetailsUpdateRequest.getTempTimeId());
				}
				classDetailsRepository.save(classDetails);
				return ResponseHandler.response(null, "Class Details Updated Successfully", true);
			} else {
				return ResponseHandler.response(null, "Class Details Not Found", false);
			}
		} else {
			return ResponseHandler.response(null, "Please Provide class details id", false);
		}
	}

	public void deleteClassForAllUsers(Long classDetailsId) throws SqlCustomException {
		String sqlQuery = "update user_class_mapping set active_flag = false where class_details_id = " + classDetailsId
				+ " and active_flag = true";
		queryExec.updateQuery(sqlQuery);
	}

	@Scheduled(cron = "@daily")
	public void revokeTempClassDetails() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		int yesterdayWeekday = calendar.get(Calendar.DAY_OF_WEEK);
		log.info("Yesterday's weekday (numeric): " + yesterdayWeekday);
		String[] weekdays = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
		String yesterdayWeekdayText = weekdays[(yesterdayWeekday - 1) % 7];
		log.info("yesterday's weekday: " + yesterdayWeekdayText);
		List<ClassDetailsEntity> tempChangedClasses = classDetailsRepository
				.tempChangedClasses(yesterdayWeekdayText.toLowerCase());
		List<ClassDetailsEntity> tempCancelledClasses = classDetailsRepository
				.tempCancelledClasses(yesterdayWeekdayText.toLowerCase());
		List<ClassDetailsEntity> classDetails = new ArrayList<>();
		if (tempChangedClasses.size() > 0) {
			tempChangedClasses.forEach(data -> {
				data.setTempChangeFlag(false);
				data.setTempTimeId(null);
				classDetails.add(data);
			});
		}
		if (tempCancelledClasses.size() > 0) {
			tempCancelledClasses.forEach(data -> {
				data.setTempCancelFlag(false);
				classDetails.add(data);
			});
		}
		classDetailsRepository.saveAll(classDetails);
	}

	@Scheduled(cron = "@monthly")
	public void revokeAllClassesForMonth() {
		List<ClassDetailsEntity> classDetails = classDetailsRepository.findActiveClasses();
		List<ClassDetailsEntity> deleteClasses = new ArrayList<>();
		System.out.println("Entered into monthly scheduler");
		if (classDetails.size() > 0) {
			classDetails.forEach(data -> {
				data.setActiveFlag(false);
				try {
					deleteClassForAllUsers(data.getClassDetailsId());
				} catch (SqlCustomException e) {

				}
				deleteClasses.add(data);
			});
			classDetailsRepository.saveAll(deleteClasses);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> classUserMapping(ClassUserMappingRequest classUserMappingRequest) {
		Boolean checkForTime = userClassMappingRepository.checkForTime(classUserMappingRequest.getUserId(),
				classUserMappingRequest.getClassDetailsId());
		if (checkForTime) {
			UserClassMappingEntity userClassMappingEntity = new UserClassMappingEntity();
			userClassMappingEntity.setActiveFlag(true);
			userClassMappingEntity.setUserId(classUserMappingRequest.getUserId());
			userClassMappingEntity.setClassDetailsId(classUserMappingRequest.getClassDetailsId());
			userClassMappingRepository.save(userClassMappingEntity);
			return ResponseHandler.response(null, "Class Joined Succesfully", true);
		} else {
			return ResponseHandler.response(null, "Can't able to join class. Class Timings are Overlapping", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> classUserLeave(ClassUserLeaveRequest classUserLeaveRequest) {
		if (classUserLeaveRequest.getUserClassMappingId() != null) {
			UserClassMappingEntity userClassMapping = userClassMappingRepository
					.getActiveMapping(classUserLeaveRequest.getUserClassMappingId());
			if (userClassMapping == null) {
				return ResponseHandler.response(null, "User Class Mapping Not Found", false);
			}
			if (userClassMapping.getUserId() == classUserLeaveRequest.getUserId()) {
				userClassMapping.setActiveFlag(false);
				userClassMappingRepository.save(userClassMapping);
				return ResponseHandler.response(null, "Leaved from class succesfully.", true);
			}
			return ResponseHandler.response(null, "Don't have permission to remove class for others", false);
		} else {
			return ResponseHandler.response(null, "Please provide Class Mapping Id", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> classUsersList(ClassDetailsGetRequest classUsersListRequest) {
		if(classUsersListRequest.getClassDetailsId()==null) {
			return ResponseHandler.response(null, "Please Provide Class Details Id", false);
		}
		List<Object[]> classUsers = userClassMappingRepository.classUsersList(classUsersListRequest.getClassDetailsId());
		JSONObject response = new JSONObject();
		if(classUsers.isEmpty()) {
			response.put("usersList", new ArrayList<>());
			return ResponseHandler.response(response.toMap(), "No Users Found for This class", false);
		}
		
		return null;
	}
}
