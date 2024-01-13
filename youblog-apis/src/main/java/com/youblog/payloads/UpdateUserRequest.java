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
	private String firstName;
	private String LastName;
	private String Gender;
	private String PersonalNo;
	private Boolean activeFlag;
}
