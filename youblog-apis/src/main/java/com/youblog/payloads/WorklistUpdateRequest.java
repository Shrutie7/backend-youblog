package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorklistUpdateRequest {

	private Long worklistDetailsId;

	private Long userId;

	private String action;
}
