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
@Table(name = "CLASS_DETAILS")


public class ClassDetailsEntity { 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CLASS_DETAILS_ID")
	private Long classDetailsId;

	@Column(name = "CLASS_MASTER_ID")
	private Long classMasterId;

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "TIME_DETAILS_ID")
	private Long timeDetailsId;

	@Column(name = "TRAINER_ID")
	private Long trainerId;

	@Column(name = "GYM_ID")
	private Long gymId;

	@Column(name = "WEEK_DAY")
	private String weekDay;

	@Column(name = "ACTIVE_FLAG")
	private Boolean activeFlag;

	@Column(name = "TEMP_CANCEL_FLAG")
	private Boolean tempCancelFlag;

	@Column(name = "TEMP_CHANGE_FLAG")
	private Boolean tempChangeFlag;

	@Column(name = "TEMP_TIME_ID")
	private Long tempTimeId;

	}