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
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TIME_DETAILS")


public class TimeDetailsEntity { 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TIME_DETAILS_ID")
	private Long timeDetailsId;

	@Column(name = "START_TIME")
	private Long startTime;

	@Column(name = "END_TIME")
	private Long endTime;

	@Column(name = "ACTIVE_FLAG")
	private Boolean activeFlag;

	}
