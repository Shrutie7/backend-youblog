package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlanEditRequest {

	private Long planId;

	private String planName;

	private Long planDuration;

	private Long planPrice;

	private String planDescription;

	private Integer[] categoryId;

	private String features;
}
