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
@Table(name = "USER_CLASS_MAPPING")


public class UserClassMappingEntity { 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_CLASS_MAPPING_ID")
	private Long userClassMappingId;

	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "CLASS_DETAILS_ID")
	private Long classDetailsId;

	@Column(name = "ACTIVE_FLAG")
	private Boolean activeFlag;

	@Column(name = "TEMP_FLAG")
	private Boolean tempFlag;

	@Column(name = "RANGE_FLAG")
	private Boolean rangeFlag;

	}