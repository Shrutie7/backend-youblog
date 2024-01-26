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
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "POST_SAVE_DETAILS")


public class PostSaveDetailsEntity { 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "POST_SAVE_DETAILS_ID")
	private Long postSaveDetailsId;

	@Column(name = "POST_ID")
	private Long postId;

	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "ACTIVE_FLAG")
	private Boolean activeFlag;

	@Column(name = "POST_SAVED_DATE")
	private Date postSavedDate;

	}