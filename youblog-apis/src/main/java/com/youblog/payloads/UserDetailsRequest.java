package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class UserDetailsRequest {

	private String emailId;
	private String firstName;
	private String lastName;
	private String gender;
	private String personalNo;
	private String password;

}
