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
@Table(name = "COMMENT_DETAILS")

public class CommentDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COMMENT_ID")
	private Long commentId;

	@Column(name = "COMMENT_DESC")
	private String commentDesc;

	@Column(name = "ACTIVE_FLAG")
	private Boolean activeFlag;

	@Column(name = "POST_ID")
	private Long postId;

	@Column(name = "COMMENT_PARENT_ID")
	private Long commentParentId;

	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "COMMENTED_DATE")
	private Date commentedDate;

}
