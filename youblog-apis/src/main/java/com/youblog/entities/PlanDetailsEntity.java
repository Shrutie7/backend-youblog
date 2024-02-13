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
@Table(name = "plan_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlanDetailsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plan_id")
	private Long planId;

	@Column(name = "plan_name")
	private String planName;

	@Column(name = "plan_price")
	private Long planPrice;

	@Column(name = "plan_description")
	private String planDescription;

	@Column(name = "category_id")
	private Integer[] categoryId;

	@Column(name = "plan_duration")
	private Long planDuration;

	@Column(name = "active_flag")
	private Boolean activeFlag;

	@Column(name = "gym_type_id")
	private Long gymTypeId;
}
