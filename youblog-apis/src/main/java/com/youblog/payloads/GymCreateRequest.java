package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GymCreateRequest {

	private String gymName;
	private String doorNo;
	private String streetLane;
	private String pincode;
	private String contact;
	private Long locationId;
	private Long ownerId;
	
	
	
	
}
