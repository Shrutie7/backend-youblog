package com.youblog.services.serviceimpl;

import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.ClassDetailsEntity;
import com.youblog.entities.ClassMasterEntity;
import com.youblog.entities.TimeDetailsEntity;
import com.youblog.payloads.ClassDetailsCreateRequest;
import com.youblog.payloads.ClassDetailsGetRequest;
import com.youblog.payloads.ClassDetailsListRequest;
import com.youblog.payloads.ClassDetailsListTrainerRequest;
import com.youblog.payloads.ClassMasterCreateRequest;
import com.youblog.payloads.ClassMasterDeleteRequest;
import com.youblog.repositories.ClassDetailsRepository;
import com.youblog.repositories.ClassMasterRepository;
import com.youblog.repositories.TimeDetailsRepository;
import com.youblog.services.ClassDetailsService;
import com.youblog.utils.DateParser;
import com.youblog.utils.ResponseHandler;

@Service
public class ClassDetailsServiceImpl implements ClassDetailsService {

	@Autowired
	private ClassMasterRepository classMasterRepository;

	@Autowired
	private TimeDetailsRepository timeDetailsRepository;

	@Autowired
	private ClassDetailsRepository classDetailsRepository;

	@Override
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
					classDetails.setEndDate(DateParser.dateParser("dd MMM yy HH:mm:ss", classDetailsCreateRequest.getEndDate()+" 23:59:59"));
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
		String response = classDetailsRepository.classDetailsList(classDetailsListRequest.getGymId(),currentDate);
		JSONObject responseObject = new JSONObject(response);
		JSONArray arr = responseObject.getJSONArray("classList");
		if(!arr.isEmpty()) {
			return ResponseHandler.response(responseObject.toMap(), "Class List Details Fetched Successfully", true);
		}else {
			return ResponseHandler.response(responseObject.toMap(), "Class List Not Found", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> classDetailsListTrainer(
			ClassDetailsListTrainerRequest classDetailsListTrainerRequest) {
		Date currentDate = Date.from(Instant.now());
		String response = classDetailsRepository.classDetailsListTrainer(classDetailsListTrainerRequest.getTrainerId(),currentDate);
		JSONObject responseObject = new JSONObject(response);
		JSONArray arr = responseObject.getJSONArray("classListTrainer");
		if(!arr.isEmpty()) {
			return ResponseHandler.response(responseObject.toMap(), "Class List Details For Trainer Fetched Successfully", true);
		}else {
			return ResponseHandler.response(responseObject.toMap(), "Class List Details For Trainer Not Found", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> classDetailsGet(ClassDetailsGetRequest classDetailsGetRequest) {
		String response = classDetailsRepository.classDetailsGet(classDetailsGetRequest.getClassDetailsId());
		JSONObject responseObject = new JSONObject();
		if(response!=null) {
			responseObject = new JSONObject(response);
			return ResponseHandler.response(responseObject.toMap(), "Class Details Fetched Successfully", true);
		}else {
			return ResponseHandler.response(responseObject.toMap(), "Class Details Not Found", false);
		}
	}

}
