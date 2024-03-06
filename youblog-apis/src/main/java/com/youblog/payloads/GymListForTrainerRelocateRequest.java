package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GymListForTrainerRelocateRequest {
	
	private Long userId;
	
	private Long relocatedLocationId;
	
	private Long categoryId;
	
}
