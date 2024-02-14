package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDetailsRequest {

	private String userName;

	private String emailId;

	private String firstName;

	private String lastName;

	private String gender;

	private String password;

	private String confirmPassword;

	private Long roleId;

	private Long parentUserId;

	private Long locationId;

	private Long categoryId;

	private Long gymId;

}
