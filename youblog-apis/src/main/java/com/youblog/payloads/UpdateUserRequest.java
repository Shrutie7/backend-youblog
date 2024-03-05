package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateUserRequest {

	private Long userId;

	private String userName;

	private String firstName;

	private String lastName;

	private String gender;

	private String emailId;

	private Long parentUserId;

	private Long locationId;

	private Boolean activeFlag;

	private Long gymId;
	
	private String image;
}
