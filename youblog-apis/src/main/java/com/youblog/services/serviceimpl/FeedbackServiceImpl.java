package com.youblog.services.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.FeedbackDetailsEntity;
import com.youblog.entities.ImageDetailsEntity;
import com.youblog.payloads.FeedbackCreateRequest;
import com.youblog.payloads.FeedbackListRequest;
import com.youblog.repositories.FeedbackDetailsRepository;
import com.youblog.repositories.ImageDetailsRepository;
import com.youblog.services.FeedbackService;
import com.youblog.utils.ResponseHandler;

@Service
public class FeedbackServiceImpl implements FeedbackService {
	@Autowired
	FeedbackDetailsRepository feedbackDetailsRepository;
	
	@Autowired
	private ImageDetailsRepository imageDetailsRepository;

	@Override
	public ResponseEntity<Map<String, Object>> createFeedback(FeedbackCreateRequest feedbackCreateRequest) {

		FeedbackDetailsEntity use = new FeedbackDetailsEntity();

		System.out.println(feedbackCreateRequest.getRating());
		if (feedbackCreateRequest.getRating() != null) {
			use.setFeedbackTypeId(feedbackCreateRequest.getFeedbackTypeId());
			use.setRating(feedbackCreateRequest.getRating());
			use.setGymId(feedbackCreateRequest.getGymId());
			use.setUserId(feedbackCreateRequest.getUserId());
			use.setTrainerUserId(feedbackCreateRequest.getTrainerId());
			use.setMessage(feedbackCreateRequest.getMessage());

			feedbackDetailsRepository.save(use);

			return ResponseHandler.response(null, "feedback created successfully", true);
		} else {
			return ResponseHandler.response(null, "Please provide rating", false);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> feedbackList(FeedbackListRequest feedbackListRequest) {
		if (feedbackListRequest.getGymId() != null) {
			List<Object[]> getfeedback = feedbackDetailsRepository.getFeedbackList(feedbackListRequest.getGymId());
			List<Map<String, Object>> feedbackDetailsList = new ArrayList<>();
			if (getfeedback != null) {
				Map<String, Object> hm = new HashMap<>();
				getfeedback.forEach(ele -> {
					hm.put("rating", ele[0] != null ? ele[0].toString() : "N/A");
					hm.put("message", ele[1] != null ? ele[1].toString() : "N/A");
					hm.put("userName", ele[2] != null ? ele[2].toString() : "N/A");
					if (ele[3] != null) {
						Optional<ImageDetailsEntity> image = imageDetailsRepository.findById(ele[3].toString());
						if (!image.isEmpty()) {
							hm.put("image", image.get().getImage() != null ? image.get().getImage() : "");
						} else {
							hm.put("image", "");
						}
					} else {
						hm.put("image", "");
					}
					feedbackDetailsList.add(hm);
				});
				return ResponseHandler.response(feedbackDetailsList, "Feedback list fetched successfully", true);
			} else {
				return ResponseHandler.response(null, "Feedback list is not found", false);
			}
		} else {
			return ResponseHandler.response(null, "Please provide gymId", false);
		}

	}

}
