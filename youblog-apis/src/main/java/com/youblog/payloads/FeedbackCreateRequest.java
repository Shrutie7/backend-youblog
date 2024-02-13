package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FeedbackCreateRequest {
	
	private Long userId;
	private Double rating;
	private Long trainerId;
	private Long gymId;
	private Long feedbackTypeId;
	private String message;
	
	

}
