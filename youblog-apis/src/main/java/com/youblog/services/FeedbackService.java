package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.youblog.payloads.FeedbackCreateRequest;
import com.youblog.payloads.FeedbackListRequest;

public interface FeedbackService {

	public ResponseEntity<Map<String, Object>> createFeedback(FeedbackCreateRequest feedbackCreateRequest);

	public ResponseEntity<Map<String, Object>> feedbackList(FeedbackListRequest feedbackListRequest);

}
