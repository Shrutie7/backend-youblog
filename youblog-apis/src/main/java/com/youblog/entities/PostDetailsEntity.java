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
@Table(name = "POST_DETAILS")


public class PostDetailsEntity { 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "POST_ID")
	private Long postId;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "CONTENT")
	private String content;

	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "CATEGORY_ID")
	private Integer[] categoryId;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "DECRIPTION")
	private String decription;
	
	@Column(name = "ACTIVE_FLAG")
	private Boolean activeFlag;

	@Column(name = "ARCHIVE_FLAG")
	private Boolean archiveFlag;
	
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;
	
	@Column(name = "UPDATED_USER_ID")
	private Long updatedUserId;
	
	@Column(name = "REMARKS")
	private String remarks;

}