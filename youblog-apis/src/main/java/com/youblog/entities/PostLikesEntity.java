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
@Table(name = "POST_LIKES")

public class PostLikesEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "POST_LIKES_ID")
	private Long postLikesId;

	@Column(name = "POST_ID")
	private Long postId;

	@Column(name = "LIKED_USER_ID")
	private Long likedUserId;

	@Column(name = "ACTIVE_FLAG")
	private Boolean activeFlag;

	@Column(name = "LIKED_ON_DATE")
	private Date likedOnDate;

}
