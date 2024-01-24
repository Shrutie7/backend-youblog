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
@Table(name = "feedback_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FeedbackDetailsEntity {

	
@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="feedback_id")
public Long feedbackId;

@Column(name="user_id")
public Long userId;

@Column(name="rating")
public Long rating;

@Column(name="trainer_user_id")
public Long trainerUserId;

@Column(name="feedback_type_id")
public Long feedbackTypeId;

@Column(name="gym_id")
public Long gymId;
	

@Column(name="message")
public String message;
	
	
}
