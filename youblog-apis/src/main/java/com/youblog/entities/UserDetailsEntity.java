package com.youblog.entities;

import java.util.Date;

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
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class UserDetailsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="email_id")
	private String emailId;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="role_id")
	private Long roleId;
	
	@Column(name="plan_id")
	private Long planId;
	
	@Column(name="parent_user_id")
	private Long parentUserId;
	
	@Column(name="location_id")
	private Long locationId;
	
	@Column(name="plan_purchased_date")
	private Date planPurchasedDate;
	
	@Column(name="gender")
	private String gender;
	
	@Column(name="active_flag")
	private boolean activeFlag;
	
	@Column(name="gym_id")
	private Long gymId;
	
	@Column(name="category_id")
	private Long categoryId;
	
	@Column(name = "image_id")
	private String imageId;
	
	@Column(name = "worklist_status")
	private String worklistStatus;
}
