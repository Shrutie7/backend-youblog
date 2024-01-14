package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdatePasswordRequest {
	private String emailId;
	private String currentPassword;
	private String newPassword;
	private String confirmNewPassword;
}
