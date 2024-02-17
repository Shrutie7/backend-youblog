package com.youblog.services.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.ClassMasterEntity;
import com.youblog.payloads.ClassMasterCreateRequest;
import com.youblog.payloads.ClassMasterDeleteRequest;
import com.youblog.repositories.ClassMasterRepository;
import com.youblog.services.ClassDetailsService;
import com.youblog.utils.ResponseHandler;

@Service
public class ClassDetailsServiceImpl implements ClassDetailsService {

	@Autowired
	private ClassMasterRepository classMasterRepository;

	@Override
	public ResponseEntity<Map<String, Object>> classMasterCreate(ClassMasterCreateRequest classMasterCreateRequest) {
		if (!classMasterCreateRequest.getClassList().isEmpty()) {
			List<ClassMasterEntity> classes = new ArrayList<>();
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
		return ResponseHandler.response(null, "No Data Classes Found", false);
	}

	@Override
	public ResponseEntity<Map<String, Object>> classMasterDelete(ClassMasterDeleteRequest classMasterDeleteRequest) {
		Optional<ClassMasterEntity> deleteClass = classMasterRepository.findById(classMasterDeleteRequest.getClassId());
		if (deleteClass.isEmpty()) {
			return ResponseHandler.response(null, "Class Not Found", false);
		}
		ClassMasterEntity delete = deleteClass.get();
		delete.setActiveFlag(false);
		classMasterRepository.save(delete);
		return ResponseHandler.response(null, "Class Deleted Successfully", true);
	}

}
