package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassDetailsCreateRequest {
	
	private Long classMasterId;
	
	private String startDate;
	
	private String endDate;
	
	private Long timeDetailsId;
	
	private Long trainerId;
	
	private Long gymId;
	
	private String weekDay;
	
}
