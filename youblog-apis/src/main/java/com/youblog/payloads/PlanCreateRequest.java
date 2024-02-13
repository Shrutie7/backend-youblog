package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlanCreateRequest {
	
	private String planName;
	private Long planPrice;
	private String planDescription;
	private Long gymTypeId;
	private Long planDuration;
	private Integer[] categoryId;	
	private String features;
}
