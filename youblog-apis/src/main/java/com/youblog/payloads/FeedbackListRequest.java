package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class FeedbackListRequest {

	private Long feedbackTypeId;

	private Long trainerUserId;

	private Long gymId;
}
