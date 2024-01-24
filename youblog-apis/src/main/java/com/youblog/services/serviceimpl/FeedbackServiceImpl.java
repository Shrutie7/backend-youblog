package com.youblog.services.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.FeedbackDetailsEntity;
import com.youblog.payloads.FeedbackCreateRequest;
import com.youblog.payloads.FeedbackListRequest;
import com.youblog.repositories.FeedbackDetailsRepository;
import com.youblog.services.FeedbackService;
import com.youblog.utils.ResponseHandler;

@Service
public class FeedbackServiceImpl implements FeedbackService {
	@Autowired
	FeedbackDetailsRepository feedbackdetailsrepo;

	@Override
	public ResponseEntity<Map<String, Object>> createFeedback(FeedbackCreateRequest usreq) {

		FeedbackDetailsEntity use = new FeedbackDetailsEntity();

		System.out.println(usreq.getRating());
		if (usreq.getRating() != null) {
			use.setFeedbackTypeId(usreq.getFeedbackTypeId());
			use.setRating(usreq.getRating());
			use.setGymId(usreq.getGymId());
			use.setUserId(usreq.getUserId());
			use.setTrainerUserId(usreq.getTrainerId());
			use.setMessage(usreq.getMessage());

			feedbackdetailsrepo.save(use);

			return ResponseHandler.response(null, "feedback created successfully", true);
		} else {
			return ResponseHandler.response(null, "Please provide rating", false);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> getfeedbacklist(FeedbackListRequest usreq) {
		if (usreq.getGymId() != null) {
			ArrayList<Object[]> getfeedback = feedbackdetailsrepo.getfeedbacklist(usreq.getGymId());
			ArrayList<Map<String, Object>> feedbackDetailsList = new ArrayList<>();
			if (getfeedback != null) {
				Map<String, Object> hm = new HashMap<>();
				getfeedback.forEach(ele -> {
					hm.put("rating", ele[0] != null ? ele[0].toString() : "N/A");
					hm.put("message", ele[1] != null ? ele[1].toString() : "N/A");
					hm.put("userName", ele[2] != null ? ele[2].toString() : "N/A");

				});
				feedbackDetailsList.add(hm);
				return ResponseHandler.response(feedbackDetailsList, "Feedback list fetched successfully", true);
			} else {
				return ResponseHandler.response(null, "Feedback list is not found", false);
			}
		} else {
			return ResponseHandler.response(null, "Please provide gymId", false);
		}

	}

}
