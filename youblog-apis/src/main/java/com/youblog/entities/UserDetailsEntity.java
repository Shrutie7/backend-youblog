package com.youblog.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "user_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class UserDetailsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="email_id")
	private String emailId;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="personal_no")
	private String personalNo;
	
	@Column(name="gender")
	private String gender;
	
	@Column(name="active_flag")
	private boolean activeFlag;
}
